package com.example.common.http

import com.says.common.network.BaseRetrofit

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
object RetrofitFac :BaseRetrofit(Api.BASE_URL){
	val iData: ApiData by lazy { retrofit.create(ApiData::class.java) }
	
}