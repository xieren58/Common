@file:Suppress("DEPRECATION")

package com.says.common.file.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider
import com.says.common.CommonContext
import com.says.common.file.CommonApi
import com.says.common.file.enum.PushFromTypeEnum
import com.says.common.file.model.PushFileLiveData
import com.says.common.utils.Common
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 *  Create by rain
 *  Date: 2021/1/4
 */
class FilePushUtils(lifecycle: LifecycleOwner) : LifecycleObserver {
    private var lifecycle: LifecycleOwner? = null

    init {
        this.lifecycle = lifecycle
        lifecycle.lifecycle.removeObserver(this)
        lifecycle.lifecycle.addObserver(this)
    }

    /**
     * 阿里云上传对象
     */
    private val client by lazy {
        OSSClient(CommonContext.context,
            CommonApi.END_POINT,
            OSSAuthCredentialsProvider(CommonApi.STS_TOKEN_URL),
            ClientConfiguration().apply {
                connectionTimeout = 60 * 1000 // 连接超时，默认15秒
                socketTimeout = 60 * 1000 // socket超时，默认15秒
                maxConcurrentRequest = 1 // 最大并发请求数，默认5个
                maxErrorRetry = 2 // 失败后最大重试次数，默认2次
            })
    }
    private var runPushPool: ExecutorService? = null

//	/**
//	 * 上传到腾讯
//	 */
//	private val transferManager by lazy {
//		CosXmlSimpleService(
//			MyApp.context, CosXmlServiceConfig.Builder()
//				.setDebuggable(true)
//				.setRegion("ap-shanghai")
//				.setConnectionTimeout(60 * 1000)
//				.builder(), ShortTimeCredentialProvider(
//				"AKIDOYTAbYitC8pGTRNzNXS7ZlhX8Vfaohur",
//				"0lHZcPxGLJ3ZrtgDWaYHk362auywpKeg",
//				300
//			))
//	}

    fun pushFile(
        initLiveData: PushFileLiveData,
        imagePath: String?,
        isOriginal: Boolean,
        pushFromType: PushFromTypeEnum
    ) {
        if (!Common.isNetConnected(CommonContext.context)) {
            initLiveData.postFail()
            return
        }
        getRunPool()?.execute {
            try {
                FilePushCommon.saveLog(1, null)
                if (imagePath.isNullOrEmpty()) {
                    initLiveData.postFail()
                    return@execute
                }

                val file =
                    if (!isOriginal && !Common.isVideoUrlStr(imagePath)) CompressUtils.compressFile(
                        CommonContext.context,
                        imagePath
                    ) else File(imagePath)
                if (file == null || !file.exists()) {
                    initLiveData.postFail()
                    return@execute
                }
                val fileToMd5 = file.fileToMd5()
                if (fileToMd5.isNullOrEmpty()) {
                    initLiveData.postFail()
                    return@execute
                }
                when (pushFromType) {
                    PushFromTypeEnum.PUSH_FILE_TO_NET -> pushFileToIntent(
                        file,
                        fileToMd5,
                        initLiveData
                    )
                    PushFromTypeEnum.PUSH_FILE_TO_TEN -> pushFileToTen(file, initLiveData)
                    else -> pushFileToAli(file, fileToMd5, initLiveData)
                }
            } catch (e: Exception) {
                initLiveData.postFail()
            }
        }
    }

    /**
     * 上传到阿里
     */
    private fun pushFileToAli(file: File, fileToMd5: String, liveData: PushFileLiveData) {
        try {
            val fileName = file.name.getRandomFileName()
            val put = com.alibaba.sdk.android.oss.model.PutObjectRequest(
                "91trial",
                "91trial/$fileName",
                file.path
            )
            put.progressCallback = OSSProgressCallback { _, currentSize, totalSize ->
                liveData.postProcess((currentSize * 100 / totalSize).toInt())
            }

            val putObject = client.putObject(put)
            if (putObject == null) {
                liveData.postFail()
                return
            }
            val eTag = putObject.eTag
            if (!fileToMd5.equals(eTag, true)) {
                liveData.postFail()
                return
            }
            liveData.postNetworkPath(client.presignPublicObjectURL(put.bucketName, put.objectKey))
        } catch (e: Exception) {
            liveData.postFail()
        }
    }

    /**
     * 上传到自己服务器
     */
    private fun pushFileToIntent(file: File, fileToMd5: String, liveData: PushFileLiveData) {
//		val id = Constants.UserCommon.getProjectBean()?.id
//		val userId = Constants.UserCommon.getUserInfo()?.userId
//		try {
//			val resultPhoto = RetrofitFac.iData.pushPhotoAsync(
//				MultipartBody.Part.createFormData("projectId", if (id.isNullOrEmpty()) "" else id),
//				MultipartBody.Part.createFormData("userId", if (userId.isNullOrEmpty()) "" else userId),
//				MultipartBody.Part.createFormData("file_name", file.name, ProgressRequestBody(file.asRequestBody(Utils.getMimeType(file.absolutePath).toMediaTypeOrNull()), object : ProgressListener {
//					override fun onProgress(hasWrittenLen: Long, totalLen: Long) {
//						L.d("pushFileLoadingTag", "network-process:${(hasWrittenLen * 100 / totalLen).toInt()}")
//						liveData.postProcess((hasWrittenLen * 100 / totalLen).toInt())
//					}
//				}))
//			).execute().resultData()?.fileId
//			if (resultPhoto.isNullOrEmpty()) {
//				liveData.postFail()
//				return
//			}
//			L.d("pushFileLoadingTag", "network-success")
//			liveData.postNetworkPath(resultPhoto)
//		} catch (e: Exception) {
//			liveData.postFail()
//		}
    }

    /**
     * 上传视频到腾讯
     */
    private fun pushFileToTen(file: File, liveData: PushFileLiveData) {
//		try {
//			val putObjectRequest = com.tencent.cos.xml.model.`object`.PutObjectRequest(
//				"91trial-1258666593",
//				file.name.getRandomFileName(),
//				file.path
//			)
//			putObjectRequest.setProgressListener { complete, target ->
//				val toInt = (complete * 100 / target).toInt()
//				liveData.postProcess(toInt)
//			}
//			val putObject = transferManager.putObject(putObjectRequest)
//			L.d("pushFileFGTag", "onSuccess-putObject:$putObject")
//			val accessUrl = putObject?.accessUrl
//			L.d("pushFileFGTag", "result:$accessUrl")
//			if (accessUrl.isNullOrEmpty()) {
//				liveData.postFail()
//				return
//			}
//			liveData.postNetworkPath(accessUrl)
//		} catch (e: Exception) {
//			liveData.postFail()
//		}

    }


    /**
     * 清空所有正在运行的腾讯云上传服务
     */
    private fun cancelTenRun() {
//		try {
//			transferManager.cancelAll()
//		} catch (e: Exception) {
//		}
    }

    private fun getRunPool(): ExecutorService? {
        if (runPushPool?.isShutdown != false) {
            runPushPool = Executors.newSingleThreadExecutor()
        }
        return runPushPool
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        cancelTenRun()
        cancel()
        cancelLife()
    }

    private fun cancelLife() {
        FilePushCommon.onLifeCancel(lifecycle)
        lifecycle = null
    }

    fun cancel() {
        runPushPool?.shutdownNow()
        runPushPool = null
    }
}