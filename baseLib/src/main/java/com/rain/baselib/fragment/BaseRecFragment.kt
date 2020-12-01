package com.rain.baselib.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.rain.baselib.R
import com.rain.baselib.adapter.BaseRecAdapter
import com.rain.baselib.common.singleClick
import com.rain.baselib.databinding.*
import com.rain.baselib.viewModel.BaseRecViewModel

/**
 *  Create by rain
 *  Date: 2020/7/7
 */
abstract class BaseRecFragment<VB:ViewBinding> : BaseFragment<VB>(), View.OnClickListener {
	abstract override val viewModel: BaseRecViewModel<*>
	private val loadHolderView by lazy { LayoutLoadingViewBinding.bind(viewBind.root) }
	private val dataHolderView by lazy { LayoutDataViewBinding.bind(viewBind.root) }
	private val errHolderView by lazy { LayoutErrViewBinding.bind(viewBind.root) }
	private val emptyHolderView by lazy { LayoutEmptyViewBinding.bind(viewBind.root) }

	override fun getViewBindLayout(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
		return  LayoutRecViewBinding.inflate(inflater,container,false)
	}
	override fun initModelObserve() {
		viewModel.loadEnd.observe(this, {
			if (it == null || it) {
				dataHolderView.rvData.isNestedScrollingEnabled = true
				finishLoad()
			} else dataHolderView.rvData.isNestedScrollingEnabled = false
		})
		viewModel.uiDataType.observe(this, {
			if (it != null) showUi(it)
		})
	}
	
	override fun initView() {
		initSmart()
		initRec()
	}
	
	private fun initRec() {
		dataHolderView.rvData.layoutManager = getRecLayoutManager()
		val itemDecoration = recItemDecoration
		if (itemDecoration != null) {
			dataHolderView.rvData.removeItemDecoration(itemDecoration)
			dataHolderView.rvData.addItemDecoration(itemDecoration)
		}
		viewModel.adapter.run {
			setOnItemClickListener(object : BaseRecAdapter.OnItemClickListener {
				override fun itemClick(position: Int) {
					clickRecItem(position)
				}
			})
			setOnItemLongClickListener(object : BaseRecAdapter.OnItemLongClickListener {
				override fun itemLongClick(position: Int) {
					itemLong(position)
				}
			})
			dataHolderView.rvData.adapter = this
		}
	}
	
	protected open val recItemDecoration: RecyclerView.ItemDecoration? = null
	abstract fun getRecLayoutManager(): RecyclerView.LayoutManager
	
	private fun initSmart() {
		dataHolderView.smartRefresh.bindRecycler(dataHolderView.rvData)
		dataHolderView.smartRefresh.setLoadRefreshMoreDataListener({
			viewModel.loadStartData(isRefresh = false, isShowLoad = false)
		}, {
			viewModel.loadStartData(true, isShowLoad = false)
		})
		setMoreRefreshEnable()
	}
	
	protected open val loadMoreEnable = true
	protected open val loadRefreshEnable = true
	
	//adapter-item-click
	abstract fun clickRecItem(position: Int)
	
	//长按事件
	open fun itemLong(position: Int) = Unit
	
	private fun setMoreRefreshEnable() {
		dataHolderView.smartRefresh.setEnableRefresh(loadRefreshEnable)//启用刷新
		dataHolderView.smartRefresh.setEnableLoadMore(loadMoreEnable)//启用加载
	}
	
	private fun finishLoad() {
		dataHolderView.smartRefresh.finishRefresh()
		dataHolderView.smartRefresh.finishLoadMore()
	}
	
	private fun showUi(value: Int) {
		loadHolderView.rlLoading.visibility = if (value == BaseRecViewModel.UI_TYPE_LOAD) View.VISIBLE else View.GONE
		emptyHolderView.rlEmpty.visibility = if (value == BaseRecViewModel.UI_TYPE_EMPTY) View.VISIBLE else View.GONE
		errHolderView.rlError.visibility = if (value == BaseRecViewModel.UI_TYPE_ERROR) View.VISIBLE else View.GONE
		dataHolderView.smartRefresh.visibility = if (value == BaseRecViewModel.UI_TYPE_DATA) View.VISIBLE else View.GONE
	}
	
	override fun initEvent() {
		super.initEvent()
		emptyHolderView.llEmpty.singleClick(this)
	}
	
	override fun onClick(v: View?) {
		when (v?.id) {
			R.id.ll_empty-> callRefreshView()
		}
	}
	
	fun callRefreshView() {
		if (dataHolderView.rvData.isComputingLayout) return
		var isShowLoad = false
		if (viewModel.isShowDataView()) {
			if (loadRefreshEnable) {
				dataHolderView.smartRefresh.autoRefresh()
				return
			}
		} else isShowLoad = true
		viewModel.loadStartData(isRefresh = true, isShowLoad = isShowLoad)
	}
}