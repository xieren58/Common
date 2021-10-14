package com.example.common.adapter

import com.example.common.BR
import com.example.common.R
import com.example.common.model.CityModel
import com.rain.baselib.adapter.BaseRecAdapter

/**
 *  Create by rain
 *  Date: 2020/12/14
 */
class ChildShowAdapter : BaseRecAdapter<CityModel>() {
    override fun getLayoutResId(viewType: Int): Int {
        return R.layout.item_child_show
    }
    
    override fun getVariableId(viewType: Int): Int {
        return BR.childShowId
    }
}