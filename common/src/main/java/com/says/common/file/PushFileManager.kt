package com.says.common.file

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.says.common.utils.Common
import com.says.common.file.listener.FilePushResultListener
import kotlinx.coroutines.*
import okhttp3.internal.and
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.util.*

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
class PushFileManager private constructor(val context: Context) : LifecycleObserver {
	companion object {
		val pushMap: MutableMap<Any, PushFileManager> = mutableMapOf()
		
		fun init(context: Context): PushFileManager {
			return if (context is FragmentActivity) {
				this.init(context)
			} else {
				var pushFileManager = pushMap[context]
				Log.d("pushTag", "pushFileManager:$pushFileManager")
				if (pushFileManager == null) {
					pushFileManager = PushFileManager(context)
					pushMap[context] = pushFileManager
				}
				pushFileManager
			}
		}
		
		fun init(activity: FragmentActivity): PushFileManager {
			var pushFileManager = pushMap[activity.baseContext]
			Log.d("pushTag", "pushFileManager:$pushFileManager")
			if (pushFileManager == null) {
				pushFileManager = PushFileManager(activity.baseContext)
				activity.lifecycle.addObserver(pushFileManager)
				pushMap[activity.baseContext] = pushFileManager
			}
			return pushFileManager
		}
		
		fun init(fragment: Fragment): PushFileManager {
			var pushFileManager = pushMap[fragment.requireContext()]
			Log.d("pushTag", "pushFileManager:$pushFileManager")
			if (pushFileManager == null) {
				pushFileManager = PushFileManager(fragment.requireContext())
				fragment.lifecycle.addObserver(pushFileManager)
				pushMap[fragment.requireContext()] = pushFileManager
			}
			return pushFileManager
		}
	}
	
	val launchList: MutableMap<String, Job> = mutableMapOf()
	val ossTaskList: MutableMap<String, OSSAsyncTask<PutObjectResult>> = mutableMapOf()
	val mainScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main + Job()) }
	
	fun get(): FileResultBuilder {
		return FileResultBuilder(this, context)
	}
	
	@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
	fun onDestroy() {
		cancelAll()
		Log.d("pushTag", "onDestroy:$context")
		val pushFileManager = pushMap[context]
		if (pushFileManager != null) pushMap.remove(context)
	}
	
	/**
	 * 取消一个任务
	 */
	fun cancel(filePath: String?) {
		removeLauncherMap(filePath)
		removeOssMap(filePath)
	}
	
	/**
	 * 取消所有任务
	 */
	private fun cancelAll() {
		Log.d("pushTag", "cancelAll:$context")
		mainScope.cancel()
		clearOssMapAll()
		launchList.clear()
	}
	
	/**
	 * 清空oss的任务
	 */
	private fun clearOssMapAll() {
		ossTaskList.forEach {
			val oss = it.value
			if (oss.isCanceled || oss.isCompleted) return@forEach
			oss.cancel()
		}
		ossTaskList.clear()
	}
	
	/**
	 * 删除当前正在运行的协程
	 */
	private fun removeLauncherMap(filePath: String?) {
		if (filePath.isNullOrEmpty()) return
		val job = launchList[filePath] ?: return
		if (!job.isActive || job.isCompleted || job.isCancelled) return
		job.cancel()
		launchList.remove(filePath)
	}
	/**
	 * 删除当前正在运行的oss
	 */
	private fun removeOssMap(filePath: String?) {
		if (filePath.isNullOrEmpty()) return
		val ossTask = ossTaskList[filePath] ?: return
		if (ossTask.isCompleted || ossTask.isCanceled) return
		ossTask.cancel()
		ossTaskList.remove(filePath)
	}
}

class FileResultBuilder(private val fileManager: PushFileManager, val context: Context) {
	private var isOriginal = false
	private var resultListener: FilePushResultListener? = null
	
	fun original(isOriginal: Boolean): FileResultBuilder {
		this.isOriginal = isOriginal
		return this
	}
	
	fun setListener(resultListener: FilePushResultListener): FileResultBuilder {
		this.resultListener = resultListener
		return this
	}
	
	fun build(filePath: String?) {
		Log.d("pushTag", "filePath:$filePath")
		
		if (filePath.isNullOrEmpty()) {
			errorPush(filePath, PushCommon.PUSH_FILE_EMPTY, "图片地址为空")
			return
		}
		Log.d("pushTag", "context:$context")
		if (!Common.isNetConnected(context)) {
			errorPush(filePath, PushCommon.PUSH_NETWORK_ERROR, "请检查网络连接")
			return
		}
		val job = fileManager.mainScope.launch {
			val file = if (!isOriginal && !Common.isVideoUrlStr(filePath)) CompressUtils.compressFile(context, filePath) else File(filePath)
			if (file == null || !file.exists()) {
				errorPush(filePath, PushCommon.PUSH_FILE_NULL, "压缩选择图片失败")
				return@launch
			}
			val fileToMd5 = file.fileToMd5()
			if (fileToMd5.isNullOrEmpty()) {
				errorPush(filePath, PushCommon.PUSH_FILE_MD5_ERROR, "获取Md5失败")
				return@launch
			}
			val ossClient = withContext(Dispatchers.IO) {
				return@withContext context.getOssClient()
			}
			val fileName = file.name.getRandomFileName()
			Log.d("chatTag", "fileName:$fileName")
			val put = PutObjectRequest(CommonApi.bucketName, "${CommonApi.objectName}/$fileName", file.path)
			//// 异步上传时可以设置进度回调
			put.progressCallback = OSSProgressCallback { request, currentSize, totalSize ->
				Log.i("PutObject", "图片路径：" + request.objectKey + "-上传进度：" + currentSize * 100 / totalSize)
				fileManager.mainScope.async(Dispatchers.Main) { resultListener?.pushProgress((currentSize * 100 / totalSize).toString().toInt()) }.start()
			}
			val ossTask = ossClient.asyncPutObject(put, object : OSSCompletedCallback<PutObjectRequest?, PutObjectResult?> {
				override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
					Log.d("chatTag", "request:$request")
					if (request == null) {
						Log.d("chatTag", "fileManager.mainScope:${fileManager.mainScope}")
						fileManager.mainScope.async(Dispatchers.Main) {
							Log.d("chatTag", "返回内容为空")
							errorPush(filePath, PushCommon.PUSH_FILE_RESULT_NULL, "返回内容为空")
						}.start()
						return
					}
					fileManager.mainScope.async(Dispatchers.Main) {
						Log.d("chatTag", "pushSuccess")
						resultListener?.pushSuccess(ossClient.presignPublicObjectURL(request.bucketName, request.objectKey))
						fileManager.cancel(filePath)
					}.start()
				}
				
				override fun onFailure(request: PutObjectRequest?, clientException: ClientException?, serviceException: ServiceException?) {
					Log.d("chatTag", "serviceException:${serviceException.toString()}")
					fileManager.mainScope.async(Dispatchers.Main) { errorPush(filePath, PushCommon.PUSH_FILE_RESULT_NULL, "阿里上传失败") }.start()
					
				}
			})
			fileManager.ossTaskList[filePath] = ossTask
		}
		Log.d("pushTag", "job:$job")
		fileManager.launchList[filePath] = job
	}
	
	private fun errorPush(filePath: String?, pushCommon: PushCommon, message: String) {
		resultListener?.pushFail(pushCommon, message)
		fileManager.cancel(filePath)
	}
}

fun String.getRandomFileName(): String {
	return "file_" + UUID.randomUUID().toString() + substring(lastIndexOf("."))
}


fun File.fileToMd5(): String? {
	Log.d("PutObject", "isFile:${isFile}")
	if (!isFile) {
		return null
	}
	val digest: MessageDigest?
	val `in`: FileInputStream?
	val buffer = ByteArray(1024)
	var len: Int
	try {
		digest = MessageDigest.getInstance("MD5")
		`in` = FileInputStream(this)
		while (`in`.read(buffer, 0, 1024).also { len = it } != -1) {
			digest.update(buffer, 0, len)
		}
		`in`.close()
	} catch (e: Exception) {
		Log.d("PutObject", "e:$e")
		return null
	}
	return digest.digest().bytesToHexString()
}

fun ByteArray.bytesToHexString(): String? {
	val stringBuilder = StringBuilder("")
	Log.d("PutObject", "src:$this")
	if (this.isEmpty()) {
		return null
	}
	for (i in this.indices) {
		val v: Int = this[i] and 0xFF
		val hv = Integer.toHexString(v)
		if (hv.length < 2) {
			stringBuilder.append(0)
		}
		stringBuilder.append(hv)
	}
	return stringBuilder.toString()
}

//在子线程
fun Context.getOssClient() = OSSClient(this, CommonApi.END_POINT, OSSAuthCredentialsProvider(CommonApi.STS_TOKEN_URL),
		ClientConfiguration().apply {
			connectionTimeout = 60 * 1000 // 连接超时，默认15秒
			socketTimeout = 60 * 1000 // socket超时，默认15秒
			maxConcurrentRequest = 5 // 最大并发请求书，默认5个
			maxErrorRetry = 2 // 失败后最大重试次数，默认2次
		})

