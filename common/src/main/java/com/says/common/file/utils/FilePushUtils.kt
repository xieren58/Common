@file:Suppress("DEPRECATION")

package com.says.common.file.utils

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider
import com.says.common.CommonContext
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
		OSSClient(CommonContext.mContext, "http://oss-cn-shanghai.aliyuncs.com", OSSAuthCredentialsProvider("http://alists.ashermed.cn/api/sts"),
				ClientConfiguration().apply {
					connectionTimeout = 60 * 1000 // 连接超时，默认15秒
					socketTimeout = 60 * 1000 // socket超时，默认15秒
					maxConcurrentRequest = 1 // 最大并发请求数，默认5个
					maxErrorRetry = 2 // 失败后最大重试次数，默认2次
				})
	}
	private var runPushPool: ExecutorService? = null
	
	
	fun pushFile(initLiveData: PushFileLiveData, imagePath: String?, isOriginal: Boolean, pushFromType: PushFromTypeEnum) {
		if (imagePath.isNullOrEmpty()) {
			initLiveData.postFail()
			return
		}
		if (!Common.isNetConnected(CommonContext.mContext)) {
			initLiveData.postFail()
			return
		}
		Log.d("pushFileFGTag", "runPushPool:${runPushPool}")
		getRunPool()?.execute {
			Log.d("pushFileLoadingTag", "--------------------------------pushFileStart--------------------------------")
			try {
				val file = if (!isOriginal && !Common.isVideoUrlStr(imagePath)) CompressUtils.compressFile(CommonContext.mContext, imagePath) else File(imagePath)
				Log.d("pushFileFGTag", "file:$file")
				if (file == null || !file.exists()) {
					initLiveData.postFail()
					return@execute
				}
				val fileToMd5 = file.fileToMd5()
				Log.d("pushFileFGTag", "fileToMd5:$fileToMd5")
				if (fileToMd5.isNullOrEmpty()) {
					initLiveData.postFail()
					return@execute
				}
				pushFileToAli(file, fileToMd5, initLiveData)
			} catch (e: Exception) {
				Log.d("pushFileFGTag", "e:$e")
				initLiveData.postFail()
			}
			Log.d("pushFileLoadingTag", "--------------------------------pushFileEnd--------------------------------")
		}
	}
	
	/**
	 * 上传到阿里
	 */
	private fun pushFileToAli(file: File, fileToMd5: String, liveData: PushFileLiveData) {
		try {
			val fileName = file.name.getRandomFileName()
			Log.d("pushFileFGTag", "fileName:$fileName")
			val put = com.alibaba.sdk.android.oss.model.PutObjectRequest("91trial", "91trial/$fileName", file.path)
			put.progressCallback = OSSProgressCallback { _, currentSize, totalSize ->
				Log.d("pushFileLoadingTag", "ali-process:${(currentSize * 100 / totalSize).toInt()}")
				liveData.postProcess((currentSize * 100 / totalSize).toInt())
			}
			
			val putObject = client.putObject(put)
			Log.d("pushFileFGTag", "putObject:$putObject")
			if (putObject == null) {
				liveData.postFail()
				return
			}
			val eTag = putObject.eTag
			Log.d("pushFileFGTag", "eTag:$eTag,requestId:${putObject.requestId}")
			if (!fileToMd5.equals(eTag, true)) {
				liveData.postFail()
				return
			}
			liveData.postNetworkPath(client.presignPublicObjectURL(put.bucketName, put.objectKey))
			Log.d("pushFileLoadingTag", "putObject-ali-success")
		} catch (e: Exception) {
			Log.d("pushFileFGTag", "e:$e")
			liveData.postFail()
		}
	}
	
	
	private fun getRunPool(): ExecutorService? {
		if (runPushPool?.isShutdown != false) {
			runPushPool = Executors.newSingleThreadExecutor()
		}
		return runPushPool
	}
	
	@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
	fun onDestroy() {
		cancel()
		cancelLife()
		Log.d("pushFileFGTag", "onDestroy")
	}
	
	private fun cancelLife() {
		FilePushManager.onLifeCancel(lifecycle)
		lifecycle = null
	}
	
	private fun cancel() {
		Log.d("pushFileFGTag", "cancel")
		runPushPool?.shutdownNow()
		runPushPool = null
	}
}