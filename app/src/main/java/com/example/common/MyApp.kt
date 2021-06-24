package com.example.common

import android.app.Application
import android.content.Context
import com.rain.baselib.BaseLibContext
import com.says.common.CommonContext
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout


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
        CommonContext.context = this
        initAsyncSdk()
     
    }
    private fun initSmartRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context?, _: RefreshLayout? -> ClassicsHeader(context) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context: Context?, _: RefreshLayout? -> ClassicsFooter(context) }
    }
    private fun initAsyncSdk() {
        Thread{
            initSmartRefreshLayout()
        }.start()
    }
}