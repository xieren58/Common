package com.example.common.fragment

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.BR
import com.example.common.R
import com.example.common.adapter.PatientLoadStateAdapter
import com.example.common.databinding.FgDemoBinding
import com.example.common.viewModel.FgDemoViewModel
import com.rain.baselib.fragment.BaseDataBindFragment
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class DemoDataBindFragment : BaseDataBindFragment<FgDemoBinding>() {
	override val layoutResId = R.layout.fg_demo
	override val viewModel by viewModels<FgDemoViewModel>()
	
	companion object {
		fun getInstance(): DemoDataBindFragment {
			return DemoDataBindFragment()
		}
	}
	
	override val variableId = BR.fgDemoId
	override fun initView() {
		initRec()
	}
	
	override fun initModelObserve() {
		super.initModelObserve()
		viewModel.liveData.observe(this, {
			if (it == false) {
				viewBind.smartRefresh.finishRefresh()
			}
		})
	}
	
	private fun initRec() {
		viewBind.recPaging.layoutManager = LinearLayoutManager(context)
		viewModel.adapter.withLoadStateHeaderAndFooter(PatientLoadStateAdapter(viewModel.adapter),PatientLoadStateAdapter(viewModel.adapter))
		viewBind.recPaging.adapter = viewModel.adapter.withLoadStateFooter(PatientLoadStateAdapter(viewModel.adapter))
		viewBind.smartRefresh.setEnableLoadMore(false)
		viewBind.smartRefresh.setOnRefreshListener {
			viewModel.adapter.refresh()
		}
	}
}