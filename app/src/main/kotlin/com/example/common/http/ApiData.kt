package com.example.common.http

import com.example.common.http.scope.BaseResponseBody
import com.example.common.model.*
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
    suspend fun getTeachHomeList(@Field("pageIndex") pageIndex: Int?): BaseResponseBody<TeachBaseModel>
    
    @GET("knowledge/category/childlist")
    suspend fun loadDemo(
            @Query("AppVersion") AppVersion: String,
            @Query("Version") Version: Double,
            @Query("self") self: Boolean,
            @Query("Platform") Platform: String,
            @Query("deviceid") deviceid: Int,
            @Query("categoryid") categoryid: Int,
            @Query("Timestamp") Timestamp: String,
    ): DemoModel
    
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
    ): BaseResponseBody<ColumnResultModel>
    
    /**
     * 获取患者列表
     */
    @GET("Patient/PatientList")
    suspend fun loadPatientList(
            @Query("pageIndex") pageIndex: Int,
            @Query("pageSize") pageSize: Int,
            @Query("searchText") searchText: String? = "",
            @Query("transformStatus") transformStatus: String? = "",
            @Query("groupId") groupId: String? = "",
            @Query("hospital") hospital: String? = "",
            @Query("patientDataStatus") patientDataStatus: String? = "",
            @Query("subStatus") subStatus: String? = "",
            @Query("ordertype") ordertype: String? = "",
            @Query("tablename") tablename: String? = "",
            @Query("isInGroup") isInGroup: String? = "",
            @Query("cureStatus") cureStatus: String? = "",
            @Query("treatStatus") treatStatus: String? = "",
            @Query("userId") userId: String = "f7b6387f-42e3-4edd-b9e3-6753b75b500c",
            @Query("projectId") projectId: String = "9bfc78a8-6d2e-47b9-94cb-c3f40dfd9dc7",
    ): BaseResponseBody<MutableList<PatientMainModel>>
}