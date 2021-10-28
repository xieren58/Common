package com.says.common.file.download

import com.says.common.network.LoggingInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 *  Create by rain
 *  Date: 2021/10/28
 */
class OkHttpUtils {
    companion object {
        private const val connectTimeOut: Long = 120
        private const val readTimeOut: Long = 60
        private const val writeTimeOut: Long = 60

        val mOkHttp by lazy {
            OkHttpClient.Builder().apply {
                connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                readTimeout(readTimeOut, TimeUnit.SECONDS)
                writeTimeout(writeTimeOut, TimeUnit.SECONDS)
                addInterceptor(LoggingInterceptor())
                retryOnConnectionFailure(true)
            }.build()
        }
    }

}