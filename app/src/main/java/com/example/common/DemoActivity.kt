package com.example.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rain.baselib.activity.BaseRecActivity
import com.says.common.utils.JsonManagerHelper

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
class DemoActivity : BaseRecActivity() {
    override val viewModel by viewModels<DemoListViewModel>()

    override fun getRecLayoutManager() = LinearLayoutManager(this)
    override val loadRefreshEnable: Boolean
        get() = false
    override val loadMoreEnable: Boolean
        get() = false

    override fun clickRecItem(position: Int) {

    }
    
    override fun init() {
        setBarColor(R.color.white)
        super.init()
    }
    override val rightStr = "确认"

    override fun rightTvClick() {
        setResult(RESULT_OK, Intent().apply {
            putExtra("demo", JsonManagerHelper.getHelper().objToStr(viewModel.getLists()))
        })
        finish()
    }
    
    override fun initIntent(savedInstanceState: Bundle?) {
        super.initIntent(savedInstanceState)
        val requestData = intent?.getStringExtra("requestData")
        Log.d("resultTag", "requestData:$requestData")
    }

    override fun initEvent() {
        super.initEvent()
        viewModel.adapter.setPhotoItemClickListener()
    }
}