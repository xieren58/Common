package com.example.common.fragment

import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.R
import com.example.common.activity.RecyclerDemoActivity
import com.example.common.activity.RecyclerListActivity
import com.example.common.databinding.ItemPatientFgViewBinding
import com.example.common.model.PatientMainModel
import com.example.common.viewModel.FgDemoViewModel
import com.rain.baselib.common.startAc
import com.rain.baselib.databinding.LayoutRecViewBinding
import com.rain.baselib.fragment.BaseRecFragment
import com.rain.baselib.holder.BaseRecHolder


/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class DemoDataBindFragment : BaseRecFragment<LayoutRecViewBinding>() {
    override val viewModel by viewModels<FgDemoViewModel>()
    
    companion object {
        fun getInstance(): DemoDataBindFragment {
            return DemoDataBindFragment()
        }
    }
    
    override fun getRecLayoutManager() = LinearLayoutManager(context)
    
    override fun clickRecItem(position: Int) {
        val patientMainModel = viewModel.getItemData(position) ?: return
        val holder = rvData?.findViewHolderForAdapterPosition(position) ?: return
        if (holder !is BaseRecHolder<*, *>) return
        val dataBind = holder.dataBind
        if (dataBind !is ItemPatientFgViewBinding) return
        startActivity(
                Intent(context, RecyclerDemoActivity::class.java).apply {
                    putExtra("name", patientMainModel.visitName)
                },
                ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), dataBind.tvName, "name").toBundle())
        
    }
    
    override fun loadMore() {
        val lists = viewModel.adapter.getLists()
        Log.d("startMoreTag", "lists:${lists?.size}")
        if (lists.isNullOrEmpty()) return
        val layoutManager = rvData?.layoutManager ?: return
        if (layoutManager !is LinearLayoutManager) return
        val findLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        Log.d("startMoreTag", "findLastVisibleItemPosition:$findLastVisibleItemPosition")
        startAc<RecyclerListActivity>("loadList" to lists, "position" to findLastVisibleItemPosition)
        requireActivity().overridePendingTransition(0, 0)
        smartRefresh?.finishLoadMore()
    }
}