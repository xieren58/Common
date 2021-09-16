package com.example.common.viewModel

import com.example.common.adapter.VP2ViewAdapter
import com.rain.baselib.viewModel.BaseViewModel

/**
 *  Create by rain
 *  Date: 2021/5/11
 */
class PatientMainFgViewModel : BaseViewModel() {
    
    val adapter by lazy { VP2ViewAdapter() }
    
    override fun initModel() {
        super.initModel()
        adapter.setData(mutableListOf("33", "3333", "#3333", "3333333"))
    }
}