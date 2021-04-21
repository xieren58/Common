package com.example.common.model

/**
 *  Create by rain
 *  Date: 2020/12/22
 */
class DemoModel {
	var Success: Boolean = false //": true,
	var Code: Int = 0 //": 200,
	var Description: String? = null // ": "成功",
	var Data: MutableList<DemoDetailModel>? = null

}
class DemoDetailModel {
	var Id: Int = -1 //": 50,
	var ChildCount: Int = 0 //": 0,
	var CoverUrl: String? = null //": "",
	var IconUrl: String? = null // ": null,
	var ParentId: Int = -1 // ": 49,
	var Title: String? = null //": "施工工艺",
	var Level: Int = 0
	override fun toString(): String {
		return "DemoDetailModel(ParentId=$ParentId, Title=$Title, Level=$Level)"
	}
}