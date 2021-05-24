package com.example.common.model

import com.google.gson.annotations.SerializedName

/**
 * 字段配置类
 */
data class ViewColumn(
	@SerializedName("Id")
	var id: String? = null,
	@SerializedName("ProjectId")
	var projectId: String? = null,
	@SerializedName("ColumnName")
	var columnName: String? = null,
	@SerializedName("EnName")
	var enName: String? = null,
	@SerializedName("MapName")
	var mapName: String? = null,
	@SerializedName("ColumnType")
	var columnType: Int = 0,
	@SerializedName("ColumnInputType")
	var columnInputType: Int = 0,
	@SerializedName("IsRequired")
	var isRequired: Int = 0,
	@SerializedName("Na")
	var na: Int? = null,
	@SerializedName("Nd")
	var nd: Int? = null,
	@SerializedName("Uk")
	var uk: Int? = null,
	@SerializedName("Uc")
	var uc: Int? = null,
	@SerializedName("MaxValue")
	var maxValue: String? = null,
	@SerializedName("MinValue")
	var minValue: String? = null,
	@SerializedName("MaxWarnvalue")
	var maxWarnValue: String? = null,
	@SerializedName("MinWarnvalue")
	var minWarnValue: String? = null,
	@SerializedName("SecondMaxValue")
	var secondMaxValue: String? = null,
	@SerializedName("SecondMinValue")
	var secondMinValue: String? = null,
	@SerializedName("SecondMaxWarnvalue")
	var secondMaxWarnValue: String? = null,
	@SerializedName("SecondMinWarnvalue")
	var secondMinWarnValue: String? = null,
	@SerializedName("Accuracy")
	var accuracy: String? = null,
	@SerializedName("ColumnLength")
	var columnLength: Int = 0,
	@SerializedName("Option")
	var option: MutableList<OptionModel>? = null,
	@SerializedName("Unit")
	var unit: MutableList<UnitData>? = null,
	@SerializedName("GenrateId")
	var generateId: String? = null,
	@SerializedName("LinedIds")
	var linedIds: String? = null,
	@SerializedName("hiddenColumn")
	var HiddenColumn: MutableList<ViewColumn>? = null,
	@SerializedName("TableColumn")
	var tableColumn: MutableList<ViewColumn>? = null,
	@SerializedName("ValueForShow")
	var valueForShow: String? = null,
	@SerializedName("CalculateColIds")
	var calculateColIds: String? = null,//自动计算赋值
	@SerializedName("ColumnImg")
	var columnImg: String? = null,
	@SerializedName("tableColRequired")
	var tableColRequired: String? = null,
	@SerializedName("TableTitle")
	var tableTitle: String? = null,
	@SerializedName("IsLimitTableRow")
	var isLimitTableRow: Int = 0,
	@SerializedName("ScaleStart")
	var scaleStart: String? = null,
	@SerializedName("ScaleEnd")
	var scaleEnd: String? = null,
	@SerializedName("RulerMin")
	var rulerMin: Int = 0,
	@SerializedName("RulerMax")
	var rulerMax: Int = 0,
	@SerializedName("Remark")
	var remark: String? = null,//备注
	@SerializedName("AudioUrl")
	var audioUrl: String? = null,
	@SerializedName("ThirdMinWarnValue")
	var thirdMinWarnValue: String? = null,
	@SerializedName("ThirdMaxWarnValue")
	var thirdMaxWarnValue: String? = null,
	@SerializedName("IsSkipVisit")
	var isSkipVisit: Int = 0,
	@SerializedName("InTimeDvpColIds")
	var inTimeDvpColIds: String? = null,
	@SerializedName("ActivityName")
	var activityName: String? = null,
	@SerializedName("Sort")
	var sort: Int = 0,
	@SerializedName("Description")
	var description:String?=null,
) : Comparable<ViewColumn> {
	override fun compareTo(other: ViewColumn): Int {
		return if (this.sort > other.sort) 1 else if (this.sort < other.sort) -1 else 0
	}
}
/**
 * 配置选项
 */
data class OptionModel(
	@SerializedName("id")
	var id: String? = null,
	@SerializedName("EnName")
	var enName: String? = null,
	@SerializedName("SelectData")
	var selectData: String? = null,//文字值
	@SerializedName("SelectValue")
	var selectValue: String? = null,//数字代码
	@SerializedName("IsNull")
	var isNull: Int = 0,
	@SerializedName("IsOther")
	var isOther: Int = 0,
	@SerializedName("SelectImg")
	var selectImg: String? = null,
) {
	@Transient
	var isCheck: Boolean = false
	@Transient
	var isNowClick: Boolean = true
}

/**
 * 单位
 */
data class UnitData(
	@SerializedName("UnitName")
	var unitName: String? = null,
	@SerializedName("IsDefaultUnit")
	var isDefaultUnit: Int = 0
){
	@Transient
	var isCheck: Boolean = false
}