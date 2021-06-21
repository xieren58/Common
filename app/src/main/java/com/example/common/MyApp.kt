package com.example.common

import android.app.Application
import android.content.Context
import com.rain.baselib.BaseLibContext
import com.says.common.CommonContext
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext


/**
 *  Create by rain
 *  Date: 2020/1/16
 */
@HiltAndroidApp
class MyApp : Application() {
    companion object {
        lateinit var context: Application
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        BaseLibContext.context = this
        CommonContext.context = this
        initAsyncSdk()
        initSmartRefreshLayout()
    }
    private fun initSmartRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context?, _: RefreshLayout? -> ClassicsHeader(context) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context: Context?, _: RefreshLayout? -> ClassicsFooter(context) }
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