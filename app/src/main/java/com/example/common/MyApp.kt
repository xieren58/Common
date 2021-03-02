package com.example.common

import android.app.Application
import com.rain.baselib.BaseLibContext
import com.says.common.CommonContext


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
        BaseLibContext.context = this
        CommonContext.mContext = this
        initAsyncSdk()
    }
    private fun initAsyncSdk() {
//        GlobalScope.launch {
//            CommonApi.initStsTokenUrl("")
//                .initEndPoint("")
//                .initBucketName("")
//                .initObjectName("")
//        }
    }
}