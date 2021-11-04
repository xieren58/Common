package com.example.common.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.common.MyApp
import com.luck.picture.lib.tools.ToastUtils
import com.rain.baselib.viewModel.BaseViewModel
import com.says.common.file.download.DownloadFileListener
import com.says.common.file.download.DownloadFileManager

/**
 *  Create by rain
 *  Date: 2021/10/28
 */
class DownloadFileViewModel : BaseViewModel() {

    val processLiveData = MutableLiveData(0)

    fun cancel(){
        DownloadFileManager.cancelJob("https://app.distribution.medcloud.cn/upload/5de65d0e24a6c8001d798c8b/android/com.jiahui.health.app_2.0.1_221102705.apk")
    }
    fun download() {

        Log.d("downloadFileTag","download")
        DownloadFileManager.initBuilder(this)
                .url("https://app.distribution.medcloud.cn/upload/5de65d0e24a6c8001d798c8b/android/com.jiahui.health.app_2.0.1_221102705.apk")
                .repeat(false)
                .listener(object : DownloadFileListener {
                    override fun onSuccess(path: String) {
                        Log.d("downloadFileTag","success:$path")
                        ToastUtils.s(MyApp.context,"下载完成:$path")
                    }

                    override fun onError(message: String) {
                        Log.d("downloadFileTag","error:$message")
                        ToastUtils.s(MyApp.context,"下载失败:$message")
                    }

                    override fun onRetry(count: Int) {
                        Log.d("downloadFileTag","retry:$count")
                        ToastUtils.s(MyApp.context,"正在重试:$count")
                    }

                    override fun onProcess(process: Int) {
                        processLiveData.value = process
                        Log.d("downloadFileTag","process:$process")
                    }

                    override fun onRepeat() {
                        ToastUtils.s(MyApp.context,"正在下载当前任务")
                        Log.d("downloadFileTag","repeat")
                    }

                })
                .build()
    }
}