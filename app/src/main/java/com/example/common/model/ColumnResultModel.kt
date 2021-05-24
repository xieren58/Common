package com.example.common.model

import com.google.gson.annotations.SerializedName
import com.says.common.utils.JsonManagerHelper

/**
 *  Create by rain
 *  Date: 2021/5/24
 */
data class ColumnResultModel(
		@SerializedName("ContentTitle")
		var contentTitle: String? = null,
		@SerializedName("DataContent")
		var dataContent: String? = null,
		@SerializedName("Version")
		var Version: String? = null,
		@SerializedName("SaveModuleBson")
		var saveModuleBson: String? = null,
		@SerializedName("OcrIsAdditional")
		var ocrIsAdditional: Int = 0,
		@SerializedName("AliAppcode")
		var aliAppcode: String? = null,
		@SerializedName("TencentSign")
		var tencentSign: String? = null,
		@SerializedName("isShowSubmit", alternate = ["IsShowSubmit"])
		var isShowSubmit: Int = 0,//1有提交按钮 0没有
//		var PatVisitData: MutableList<VisitSelectBean>? = null

) {
	
	val content: ConfigChildEntity? by lazy { JsonManagerHelper.getHelper().strToObj(dataContent, ConfigChildEntity::class.java) }
}

data class ConfigChildEntity(
		var VisitId: String? = null,
		var VisitName: String? = null,
		var ModuleId: String? = null,
		var ModuleName: String? = null,
		var Fields: MutableList<ViewColumn>? = null,
)