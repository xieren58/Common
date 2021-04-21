package com.example.common.model

import com.google.gson.annotations.SerializedName

/**
 *  Create by rain
 *  Date: 2021/4/8
 */
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
){}