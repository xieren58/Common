package com.example.common.adapter

import com.example.common.BR
import com.example.common.R
import com.example.common.model.VisitMainModel
import com.rain.baselib.adapter.BaseRecAdapter

/**
 * 受试者列表的adapter
 */
class PatientMainAdapter :BaseRecAdapter<VisitMainModel>() {
	override fun getLayoutResId(viewType: Int) = R.layout.item_patient_main_view
	override fun getVariableId(viewType: Int) = BR.visitMainModelId
}