package com.example.common.adapter

import com.example.common.BR
import com.example.common.R
import com.example.common.model.PatientMainModel
import com.rain.baselib.adapter.BaseRecAdapter

/**
 *  Create by rain
 *  Date: 2021/4/21
 */
class PatientLoadStateAdapter : BaseRecAdapter<PatientMainModel>() {
    override fun getLayoutResId(viewType: Int) = R.layout.item_patient_fg_view
    override fun getVariableId(viewType: Int) = BR.patientMainModelId
}


