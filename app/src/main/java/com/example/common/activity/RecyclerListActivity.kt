package com.example.common.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.databinding.ItemPatientFgViewBinding
import com.example.common.viewModel.RecyclerListViewModel
import com.rain.baselib.activity.BaseRecActivity
import com.rain.baselib.databinding.ActivityBaseRecBinding
import com.rain.baselib.holder.BaseRecHolder

/**
 *  Create by rain
 *  Date: 2021/6/28
 */
class RecyclerListActivity : BaseRecActivity<ActivityBaseRecBinding>() {
    override val viewModel by viewModels<RecyclerListViewModel>()
    override fun getRecLayoutManager() = LinearLayoutManager(this)
    
    override fun initIntent(savedInstanceState: Bundle?) {
        super.initIntent(savedInstanceState)
        viewModel.initList(intent)
    }
    
    override fun initData() {
        super.initData()
        rvData?.scrollToPosition(viewModel.normalPosition)
        loadMore()
    }
    
    override fun clickRecItem(position: Int) {
        val patientMainModel = viewModel.getItemData(position) ?: return
        val holder = rvData?.findViewHolderForAdapterPosition(position) ?: return
        if (holder !is BaseRecHolder<*, *>) return
        val dataBind = holder.dataBind
        if (dataBind !is ItemPatientFgViewBinding) return
        startActivity(
                Intent(this, RecyclerDemoActivity::class.java).apply {
                    putExtra("name", patientMainModel.visitName)
                },
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, dataBind.tvName, "name")
                        .toBundle()
        )
    }
    
    
}