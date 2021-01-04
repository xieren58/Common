package com.example.common.http

import com.example.common.model.BaseResponse
import com.example.common.model.ConfigResultModel
import com.example.common.model.DemoModel
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
interface ApiData {

    @GET("kenya/public/index.php/config")
    suspend fun loadConfig(): BaseResponse<ConfigResultModel>
    
    @GET("knowledge/category/childlist")
    suspend fun loadDemo(@Query("AppVersion")AppVersion:String,
                         @Query("Version")Version:Double,
                         @Query("self")self:Boolean,
                         @Query("Platform")Platform:String,
                         @Query("deviceid")deviceid:Int,
                         @Query("categoryid")categoryid:Int,
                         @Query("Timestamp")Timestamp:String):DemoModel
}