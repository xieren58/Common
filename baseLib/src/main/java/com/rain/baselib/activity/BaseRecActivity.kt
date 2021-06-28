package com.rain.baselib.activity

import android.view.View
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.rain.baselib.R
import com.rain.baselib.common.singleClick
import com.rain.baselib.viewModel.BaseRecViewModel
import com.rain.baselib.weight.ScrollOtherRefreshLayout
import com.rain.baselib.weight.ToolbarLayout

/**
 *  RecyclerView的基类
 *  在默认的布局 [R.layout.activity_base_rec] 下传入[ActivityBaseRecBinding]
 *  重写 [layoutResId] 必须设置相对的viewDataBinding
 */
abstract class BaseRecActivity<T : ViewDataBinding> : BaseDataBindActivity<T>(),
	View.OnClickListener {
	override val layoutResId = R.layout.activity_base_rec

	/**
	 *  是否打开上拉加载更多
	 */
	protected open val loadMoreEnable = true
	abstract override val viewModel: BaseRecViewModel<*>

	/**
	 *  是否打开下拉刷新
	 */
	protected open val loadRefreshEnable = true


	/**
	 * 加载中布局
	 */
	private val rlLoading: View? by lazy { viewBind.root.findViewById(R.id.rl_loading) }
//	private val loadingView by lazy { LayoutLoadingViewBinding.bind(viewBind.root) }

	/**
	 * 数据集合布局
	 */
//	private val dataView by lazy { LayoutDataViewBinding.bind(viewBind.root) }
	val rvData: RecyclerView? by lazy { viewBind.root.findViewById(R.id.rv_data) }
	private val smartRefresh: ScrollOtherRefreshLayout? by lazy { viewBind.root.findViewById(R.id.smart_refresh) }

	/**
	 * 错误布局
	 */
	private val rlError: View? by lazy { viewBind.root.findViewById(R.id.rl_error) }
//	private val errViewClick: View? by lazy { viewBind.root.findViewById(R.id.tv_error) }

	/**
	 * 空布局
	 */
//	private val emptyView by lazy { LayoutEmptyViewBinding.bind(viewBind.root) }
	private val rlEmpty: View? by lazy { viewBind.root.findViewById(R.id.rl_empty) }
	private val emptyClickView: View? by lazy { viewBind.root.findViewById(R.id.ll_empty) }
//	private val emptyText: TextView? by lazy { viewBind.root.findViewById(R.id.tv_empty_text) }

	/**
	 * 标题栏布局
	 */
	private val toolbar: ToolbarLayout? by lazy { findViewById(R.id.toolbar) }
	override fun initModelObserve() {
		viewModel.loadEnd.observe(this, {
			rvData?.isNestedScrollingEnabled = if (it == null || it) {
				finishLoad()
				true
			} else false
		})
		viewModel.uiDataType.observe(this, {
			if (it != null) showUi(it)
		})
	}

	fun setTvContent() {
		val titleContent = getTitleContent()
		if (!titleContent.isNullOrEmpty()) {
			toolbar?.setTitleStr(titleContent)
		}
	}

	/**
	 * 设置展示的view
	 */
	fun showUi(value: Int) {
		rlLoading?.visibility = if (value == BaseRecViewModel.UI_TYPE_LOAD) View.VISIBLE else View.GONE
		rlEmpty?.visibility = if (value == BaseRecViewModel.UI_TYPE_EMPTY) View.VISIBLE else View.GONE
		rlError?.visibility = if (value == BaseRecViewModel.UI_TYPE_ERROR) View.VISIBLE else View.GONE
		smartRefresh?.visibility = if (value == BaseRecViewModel.UI_TYPE_DATA) View.VISIBLE else View.GONE
	}

	@CallSuper
	override fun initView() {
//		emptyText?.text = getEmptyText()
		initToolbar()
		initSmart()
		initRec()
	}

	/**
	 * 初始化recyclerView
	 */
	private fun initRec() {
		rvData?.layoutManager = getRecLayoutManager()
		loadRecItemDecoration()?.run {
			rvData?.removeItemDecoration(this)
			rvData?.addItemDecoration(this)
		}
		viewModel.adapter.run {
			setOnItemClickListener { clickRecItem(it) }
			setOnItemLongClickListener { itemLong(it) }
			rvData?.adapter = this
		}
	}

	/**
	 * 设置recyclerView的分割线
	 */
	protected open fun loadRecItemDecoration(): RecyclerView.ItemDecoration? = null

	/**
	 * 设置recycler的布局管理器
	 */
	abstract fun getRecLayoutManager(): RecyclerView.LayoutManager

	open fun getEmptyText() = "暂无数据"

	/**
	 * 初始化下拉刷新控件
	 */
	private fun initSmart() {
		smartRefresh?.bindRecycler(rvData)
		smartRefresh?.setLoadRefreshMoreDataListener(refreshBlock = {
			viewModel.loadStartData(true, isShowLoad = false)
		}, loadMoreBlock = {
			loadMore()
		})
		setMoreRefreshEnable()
	}
	open fun loadMore(){
		viewModel.loadStartData(isRefresh = false, isShowLoad = false)
	}
	/**
	 * 初始化标题栏
	 */
	private fun initToolbar() {
		toolbar?.initToolbar(this)
		toolbar?.setTitleStr(getTitleContent())
		toolbar?.setLeftClickListener { finish() }
		toolbar?.setShowRightImage(rightIcon?.run {
			toolbar?.setRightIcon(this)
			true
		} ?: false)

		toolbar?.setShowRightTv(
			if (rightStr.isNullOrEmpty()) false
			else {
				toolbar?.setRightStr(rightStr)
				true
			}
		)
		toolbar?.setRightTvClickListener { rightTvClick() }
		toolbar?.setRightImageClickListener { rightIconClick() }
	}

	/**
	 * 设置标题栏右侧图标 id
	 */
	open val rightIcon: Int? = null

	/**
	 * 设置标题栏右侧文字
	 */
	open val rightStr: String? = null

	/**
	 * 设置标题栏文字
	 */
	open fun getTitleContent(): String? = null


	/**
	 * 设置下拉刷新和上拉加载开关
	 */
	private fun setMoreRefreshEnable() {
		smartRefresh?.setEnableRefresh(loadRefreshEnable)//启用刷新
		smartRefresh?.setEnableLoadMore(loadMoreEnable)//启用加载
		viewModel.setMoreLoadEnable(loadMoreEnable)
	}

	/**
	 * 结束下拉刷新
	 */
	private fun finishLoad() {
		smartRefresh?.finishRefresh()
		smartRefresh?.finishLoadMore()
	}

	override fun initEvent() {
		super.initEvent()
		emptyClickView?.singleClick(this)
//		errViewClick?.singleClick(this)
	}

	/**
	 * 提供子类调用刷新页面，重新请求数据的方法
	 */
	open fun callRefreshView() {
		if (rvData?.isComputingLayout == true) return
		var isShowLoad = false
		if (viewModel.isShowDataView()) {
			if (loadRefreshEnable) {
				smartRefresh?.autoRefresh()
				return
			}
		} else isShowLoad = true
		viewModel.loadStartData(isRefresh = true, isShowLoad = isShowLoad)
	}

	/**
	 * recycler的item点击事件
	 */
	abstract fun clickRecItem(position: Int)

	/**
	 * recycler的item长按
	 */
	open fun itemLong(position: Int) = Unit

	override fun onClick(v: View?) {
		when (v?.id) {
			R.id.ll_empty,
//			R.id.tv_error,
			-> callRefreshView()
		}
	}

	/**
	 * 右侧文字点击
	 */
	open fun rightTvClick() = Unit

	/**
	 * 右侧图标点击
	 */
	open fun rightIconClick() = Unit

}