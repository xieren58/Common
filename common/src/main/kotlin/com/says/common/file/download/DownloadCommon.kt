package com.says.common.file.download

import android.os.Environment
import android.util.Log
import com.says.common.CommonContext
import com.says.common.file.utils.fileToMd5
import com.says.common.utils.PermissionUtils
import kotlinx.coroutines.*
import okhttp3.Request
import okio.*
import java.io.*
import java.security.Permission
import java.security.Permissions

/**
 * 文件下载
 */
class DownloadCommon(val builder: DownloadBuilder) {
    
    private var retryCount: Int = 0 //重试次数
    private var job: Job? = null
    
    /**
     * 开始下载
     */
    fun download(lifeScope: CoroutineScope) {
        job = lifeScope.launch(Dispatchers.IO) {
            val url = builder.getUrl()
            if (url.isNullOrEmpty()) {
                throw Exception("下载地址为空")
            }
            if (!PermissionUtils.checkPermissionPermission(CommonContext.context,
                            android.Manifest.permission.INTERNET,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                withContext(Dispatchers.Main) {
                    errorCall("获取权限失败")
                }
                return@launch
            }
            downloadFile(url)
        }
        
    }
    
    /**
     * 文件下载，写入
     */
    private suspend fun downloadFile(url: String) {
        withContext(Dispatchers.Main) {
            builder.getListener()?.onBefore()
        }
        var inputBuffer: BufferedSource? = null
        var sink: BufferedSink? = null
        try {
            val response = OkHttpUtils.mOkHttp.newCall(Request.Builder().url(url = url).get().build()).execute() //请求接口获取下载数据
            val body = response.body
            Log.d("downloadTag", "body:$body,isSuccessful:${response.isSuccessful},")
            //当下载不成功时调用重试机制
            if (!response.isSuccessful || body == null) {
                retryDownload(url, response.message)
                return
            }
            val saveDir = File(getSavePath())
            //如果文件夹不存在则创建
            if (!saveDir.exists()) {
                saveDir.mkdirs()
            }
            Log.d("downloadTag", "saveDir:${saveDir.path}")
            val saveFile = File(saveDir, getApkNameByDownloadUrl(url))
            //如果下載的文件存在則刪除
            if (saveFile.exists()) {
                saveFile.delete()
            }
            Log.d("downloadTag", "saveFile:${saveFile.path}")
            var len: Long
            var currentLength = 0L
            //读取缓冲区
            val contentLength = body.contentLength()
            
            val bufferSize = 200 * 1024L
            //使用okio來读写文件
            sink = saveFile.sink().buffer()
            val buffer = sink.buffer
            
            inputBuffer = body.byteStream().source().buffer()
            var percent = 0
            while (inputBuffer.read(buffer, bufferSize).also { len = it } != -1L) {
                sink.emit()
                currentLength += len
                if ((currentLength * 100 / contentLength).toInt() > percent) {
                    percent = (currentLength * 100 / contentLength).toInt()
                    //切换到主线程更新UI
                    Log.d("downloadTag", "percent:$percent")
                    withContext(Dispatchers.Main) {
                        processCall(process = percent)
                    }
                }
            }
            
            Log.d("downloadTag", "saveFile:${saveFile.path}")
            Log.d("downloadTag", "md5Str:${builder.getMd5()}")
            //最终结果回调
            if (!builder.getMd5().isNullOrEmpty()) {
                val fileToMd5 = saveFile.fileToMd5()
                Log.d("downloadTag", "fileToMd5:$fileToMd5")
                Log.d("downloadTag", "isEq:${builder.getMd5() == fileToMd5}")
                if (!fileToMd5.isNullOrEmpty() && builder.getMd5() == fileToMd5) {
                    withContext(Dispatchers.Main) { successCall(saveFile.path) }
                } else {
                    if (contentLength != saveFile.length()) retryDownload(url, "文件下载不完整") else {
                        withContext(Dispatchers.Main) { successCall(saveFile.path) }
                    }
                }
            } else {
                if (contentLength != saveFile.length()) retryDownload(url, "文件下载不完整") else {
                    withContext(Dispatchers.Main) { successCall(saveFile.path) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("downloadTag", "e:$e")
            if (e is CancellationException) {
                //协程已取消不在处理事务
                DownloadFileManager.removeList(this@DownloadCommon)
            } else {
                retryDownload(url, e.message ?: "文件读写错误")
            }
            
        } finally {
            inputBuffer?.close()
            sink?.close()
        }
        
    }
    
    private suspend fun retryDownload(url: String, message: String) {
        Log.d("downloadTag", "retryCount:$retryCount,maxCount:${builder.getRetryMaxCount()}")
        if (retryCount >= builder.getRetryMaxCount()) {
            withContext(Dispatchers.Main) {
                errorCall(message)
            }
        } else {
            retryCount++
            withContext(Dispatchers.Main) {
                retryCall(retryCount)
            }
            downloadFile(url)
        }
    }
    
    fun cancel() {
        job?.cancel()
    }
    
    private fun getSavePath(): String {
        val savePath = builder.getSavePath()
        return if (savePath.isNullOrEmpty()) CommonContext.context.externalCacheDir?.absolutePath
                ?: CommonContext.context.getExternalFilesDir("download")?.absolutePath
                ?: Environment.getExternalStorageState() + File.separator + CommonContext.context.packageName else savePath
    }
    
    private fun successCall(path: String) {
        builder.getListener()?.onSuccess(path)
        DownloadFileManager.removeList(this)
//        listener.remove(urlTag)
    }
    
    private fun errorCall(message: String) {
        builder.getListener()?.onError(message)
        DownloadFileManager.removeList(this)
//        listener.remove(urlTag)
    }
    
    private fun retryCall(count: Int) {
        builder.getListener()?.onRetry(count)
    }
    
    private fun processCall(process: Int) {
        builder.getListener()?.onProcess(process)
    }
    
    /**
     * 根据下载地址获取文件名
     *
     * @param url 下载地址
     * @return
     */
    private fun getApkNameByDownloadUrl(url: String): String {
        val fileName = builder.getFileName()
        if (!fileName.isNullOrEmpty()) return fileName
        return url.substring(url.lastIndexOf("/") + 1)
    }
}