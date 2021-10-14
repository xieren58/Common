package com.says.common.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("interceptTag", "----------------- start --------------------")
        val request = chain.request()
        Log.d("interceptTag", "body:${request.body}")

        Log.d("interceptTag", "url:${request.url}")
        val proceed = chain.proceed(request)
        Log.d("interceptTag", "code:${proceed.code},message:${proceed.message}")
        Log.d("interceptTag", "code:${proceed.body.toString()}")
        Log.d("errTag", "----------------- end --------------------")
        return proceed
    }

}