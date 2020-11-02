package com.example.common

import android.app.Application
import com.says.common.CommonApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 *  Create by rain
 *  Date: 2020/1/16
 */
class MyApp : Application() {
    companion object {
        lateinit var context: Application
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        initAsyncSdk()
    }
    private fun initAsyncSdk() {
        GlobalScope.launch {
            CommonApi.initStsTokenUrl("http://alists.ashermed.cn/api/sts")
                .initEndPoint("http://oss-cn-shanghai.aliyuncs.com")
                .initBucketName("91trial")
                .initObjectName("91trial")
        }
    }
}