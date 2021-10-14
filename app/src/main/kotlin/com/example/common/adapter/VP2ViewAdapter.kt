package com.example.common.adapter

import com.example.common.BR
import com.example.common.R
import com.rain.baselib.adapter.BaseRecAdapter

/**
 *  Create by rain
 *  Date: 2021/8/31
 */
class VP2ViewAdapter : BaseRecAdapter<String>() {
    override fun getLayoutResId(viewType: Int) = R.layout.item_pager_scroll_view
    override fun getVariableId(viewType: Int) = BR.vp2ViewId
}