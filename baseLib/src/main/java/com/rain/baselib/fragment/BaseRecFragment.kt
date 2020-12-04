package com.rain.baselib.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.rain.baselib.R
import com.rain.baselib.adapter.BaseRecAdapter
import com.rain.baselib.common.singleClick
import com.rain.baselib.databinding.*
import com.rain.baselib.viewModel.BaseRecViewModel

/**
 *  RecyclerView的基类，默认传入[ActivityBaseRecBinding]，[ActivityBaseRecBinding]
 *  必须存在布局
 *   [LayoutLoadingViewBinding],[LayoutDataViewBinding],[LayoutErrViewBinding],[LayoutEmptyViewBinding]
 *
 *   VM:子类必须继承自[BaseRecViewModel]以便进行数据绑定已经设置数据
 */
abstract class BaseRecFragment<VB : ViewBinding, VM : BaseRecViewModel<*>> : BaseFragment<VB, VM>(), View.OnClickListener {
	
	/// 子类传入布局必须包含下列四种布局，否则报错
	/**
	 * 加载中布局
	 */
	private val loadHolderView by lazy { LayoutLoadingViewBinding.bind(viewBind.root) }
	
	/**
	 * 数据集合布局
	 */
	private val dataHolderView by lazy { LayoutDataViewBinding.bind(viewBind.root) }
	
	/**
	 * 错误布局
	 */
	private val errHolderView by lazy { LayoutErrViewBinding.bind(viewBind.root) }
	
	/**
	 * 空布局
	 */
	private val emptyHolderView by lazy { LayoutEmptyViewBinding.bind(viewBind.root) }
	
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
	
	/**
	 * 初始化recyclerView
	 */
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
	
	/**
	 * 设置recyclerView的分割线
	 */
	protected open val recItemDecoration: RecyclerView.ItemDecoration? = null
	
	/**
	 * 设置recycler的布局管理器
	 */
	abstract fun getRecLayoutManager(): RecyclerView.LayoutManager
	
	/**
	 * 初始化下拉刷新控件
	 */
	private fun initSmart() {
		dataHolderView.smartRefresh.bindRecycler(dataHolderView.rvData)
		dataHolderView.smartRefresh.setLoadRefreshMoreDataListener({
			viewModel.loadStartData(isRefresh = false, isShowLoad = false)
		}, {
			viewModel.loadStartData(true, isShowLoad = false)
		})
		setMoreRefreshEnable()
	}
	
	/**
	 *  是否打开上拉加载更多
	 */
	protected open val loadMoreEnable = true
	/**
	 *  是否打开下拉刷新
	 */
	protected open val loadRefreshEnable = true
	
	/**
	 * recycler的item点击事件
	 */
	abstract fun clickRecItem(position: Int)
	
	/**
	 * recycler的item长按
	 */
	open fun itemLong(position: Int) = Unit
	
	/**
	 * 设置下拉刷新和上拉加载开关
	 */
	private fun setMoreRefreshEnable() {
		dataHolderView.smartRefresh.setEnableRefresh(loadRefreshEnable)//启用刷新
		dataHolderView.smartRefresh.setEnableLoadMore(loadMoreEnable)//启用加载
	}
	
	/**
	 * 结束下拉刷新
	 */
	private fun finishLoad() {
		dataHolderView.smartRefresh.finishRefresh()
		dataHolderView.smartRefresh.finishLoadMore()
	}
	
	/**
	 * 设置展示的view
	 */
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
			R.id.ll_empty -> callRefreshView()
		}
	}
	
	/**
	 * 提供子类调用刷新页面，重新请求数据的方法
	 */
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