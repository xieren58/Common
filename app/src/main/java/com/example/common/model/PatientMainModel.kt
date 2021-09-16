package com.example.common.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *  Create by rain
 *  Date: 2021/4/8
 */
@Parcelize
data class PatientMainModel(
        @SerializedName("Id")
        var id: String?,
        @SerializedName("PatientId")
        var patientId: String?,
        @SerializedName("PatientName")
        var patientName: String?,
        @SerializedName("Sex")
        var sex: String?,
        @SerializedName("VistName")
        var visitName: String?,
        @SerializedName("HosId")
        var hosId: String? = null,// ": "3494556c-f14b-4d7b-9320-9c0562b550cc",
        @SerializedName("PatientNumber")
        var patientNumber: String? = null,// ": "Hhh",
        @SerializedName("DoubtImg")
        var doubtImg: String? = null,// ": "Hhh",
) : Parcelable


/**
 * 拜访记录列表数据
 */
data class VisitMainListResultModel(
        var visitRecords: MutableList<PatientMainModel>? = null,
        var dataCount: Int = 0,
        var minId: Long = 0,
)

data class VisitMainModel(
        var name: String?, //姓名
        var id: Long, //表示id
        var visitRecordState: Int = 0,//进行中状态
        var number: String? = null,
        var sex: Int = 0,
        var sys_CreateTime: String? = null,
        var age: String? = null,
)