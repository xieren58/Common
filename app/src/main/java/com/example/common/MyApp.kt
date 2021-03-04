package com.example.common

import android.app.Application
import com.rain.baselib.BaseLibContext
import com.says.common.CommonContext
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor


/**
 *  Create by rain
 *  Date: 2020/1/16
 */
class MyApp : Application() {
    companion object {
        lateinit var context: Application
        lateinit var flutterEngine: FlutterEngine
    }

    override fun onCreate() {
        super.onCreate()
        flutterEngine =  FlutterEngine(this).apply {
            dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        }
        FlutterEngineCache.getInstance().put("flutter_address_id",flutterEngine)
        context = this
        BaseLibContext.context = this
        CommonContext.context = this
        initAsyncSdk()
    }
    
    override fun onTerminate() {
        flutterEngine.destroy()
        super.onTerminate()
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