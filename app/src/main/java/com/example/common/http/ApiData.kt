package com.example.common.http

import com.example.common.model.BaseResponse
import com.example.common.model.ConfigResultModel
import retrofit2.http.GET

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
interface ApiData {

    @GET("kenya/public/index.php/config")
    suspend fun loadConfig(): BaseResponse<ConfigResultModel>
}