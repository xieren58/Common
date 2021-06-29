package com.example.common.viewModel

import com.example.common.adapter.PatientMainAdapter
import com.example.common.model.VisitMainModel
import com.rain.baselib.viewModel.BaseRecViewModel

/**
 *  Create by rain
 *  Date: 2021/5/11
 */
class PatientMainFgViewModel : BaseRecViewModel<VisitMainModel>() {
	override val adapter by lazy { PatientMainAdapter() }
	private var minId: Long = 0
	override fun resetPageIndex() {
		super.resetPageIndex()
		minId = 0
	}
	
	override fun loadSuccess(list: MutableList<VisitMainModel>?) {
		if (minId <= 0 && pageIndex <= 1) adapter.setData(list) else adapter.addItemData(list)
		if (!list.isNullOrEmpty()) pageIndex++
		loadEnd.value = true
		showDataType()
	}
	
	override fun loadData() {
		loadFail()
	}
}