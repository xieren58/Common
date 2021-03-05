package com.says.common.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
abstract class BaseRetrofit(baseUrl:String,vararg interceptor: Interceptor) {
    open val connectTimeOut: Long = 120
    open val readTimeOut: Long = 60
    open val writeTimeOut: Long = 60

    private val builder by lazy {
        OkHttpClient.Builder().apply {
            connectTimeout(connectTimeOut, TimeUnit.SECONDS)
            readTimeout(readTimeOut, TimeUnit.SECONDS)
            writeTimeout(writeTimeOut, TimeUnit.SECONDS)
            interceptor.forEach {
                addInterceptor(it)
            }
            addInterceptor(LoggingInterceptor())
            retryOnConnectionFailure(true)
        }
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder().apply {
            baseUrl(baseUrl)
            client(builder.build())
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }
}