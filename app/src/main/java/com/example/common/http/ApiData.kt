package com.example.common.http

import com.example.common.model.BaseResponse
import com.example.common.model.ConfigResultModel
import com.example.common.model.DemoModel
import com.example.common.model.TeachBaseModel
import retrofit2.http.*

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
interface ApiData {
    
    /**
     * 获取首页患教列表
     */
    @FormUrlEncoded
    @POST("apt/api/portal/suffering/list")
    suspend fun getTeachHomeList(@Field("pageIndex") pageIndex: Int?): BaseResponse<TeachBaseModel>
    
    @GET("knowledge/category/childlist")
    suspend fun loadDemo(@Query("AppVersion")AppVersion:String,
                         @Query("Version")Version:Double,
                         @Query("self")self:Boolean,
                         @Query("Platform")Platform:String,
                         @Query("deviceid")deviceid:Int,
                         @Query("categoryid")categoryid:Int,
                         @Query("Timestamp")Timestamp:String):DemoModel
}