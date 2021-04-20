package com.example.common.http

import com.example.common.model.BaseResponse
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
    
    /**
     * 拉取配置
     */
    @GET("Config/ColumnConfig")
    suspend fun getColumnConfig(
            @Query("projectId") projectId: String?,
            @Query("patientId") patientId: String?,
            @Query("id") moduleId: String?,
            @Query("userId") userId: String?,
            @Query("dataId") dataId: String?,
            @Query("roleId") roleId: String?,
            @Query("contentRange") contentRange: String,
            @Query("contentCode") contentCode: String,
    ): BaseResponse<Any>
}