package com.rain.baselib.activity

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.rain.baselib.R
import com.rain.baselib.adapter.BaseRecAdapter
import com.rain.baselib.common.singleClick
import com.rain.baselib.databinding.*
import com.rain.baselib.viewModel.BaseRecViewModel

/**
 *  RecyclerView的基类，默认传入[ActivityBaseRecBinding]，[ActivityBaseRecBinding]
 *	必须存在布局
 *  [LayoutLoadingViewBinding],[LayoutDataViewBinding],[LayoutErrViewBinding],[LayoutEmptyViewBinding],[LayoutBaseTitleBinding]
 *
 *  VM:子类必须继承自[BaseRecViewModel]以便进行数据绑定已经设置数据
 */
abstract class BaseRecActivity<T : ViewBinding, VM : BaseRecViewModel<*>> : BaseActivity<T, VM>(), View.OnClickListener {
	/**
	 *  是否打开上拉加载更多
	 */
	protected open val loadMoreEnable = true
	
	/**
	 *  是否打开下拉刷新
	 */
	protected open val loadRefreshEnable = true
	
	/// 子类传入布局必须包含下列五种布局，否则报错
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
	
	/**
	 * 标题栏布局
	 */
	private val titleBind by lazy { LayoutBaseTitleBinding.bind(viewBind.root) }
	
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
	
	/**
	 * 设置展示的view
	 */
	private fun showUi(value: Int) {
		loadHolderView.rlLoading.visibility = if (value == BaseRecViewModel.UI_TYPE_LOAD) View.VISIBLE else View.GONE
		emptyHolderView.rlEmpty.visibility = if (value == BaseRecViewModel.UI_TYPE_EMPTY) View.VISIBLE else View.GONE
		errHolderView.rlError.visibility = if (value == BaseRecViewModel.UI_TYPE_ERROR) View.VISIBLE else View.GONE
		dataHolderView.smartRefresh.visibility = if (value == BaseRecViewModel.UI_TYPE_DATA) View.VISIBLE else View.GONE
	}
	
	@CallSuper
	override fun initView() {
		initToolbar()
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
	 * 初始化标题栏
	 */
	private fun initToolbar() {
		titleBind.tvTitle.text = getTitleContent()
		titleBind.toolbar.setNavigationIcon(R.drawable.arrow_left)
		titleBind.toolbar.title = ""
		setSupportActionBar(titleBind.toolbar)
		titleBind.toolbar.setNavigationOnClickListener { finish() }
		
		val rightIcon = getRightIcon()
		if (rightIcon == null) {
			titleBind.igRight.visibility = View.GONE
		} else {
			titleBind.igRight.setImageResource(rightIcon)
			titleBind.igRight.visibility = View.VISIBLE
		}
		val rightStr = getRightStr()
		if (rightStr.isNullOrEmpty()) {
			titleBind.tvRight.visibility = View.GONE
		} else {
			titleBind.tvRight.text = rightStr
			titleBind.tvRight.visibility = View.VISIBLE
		}
	}
	
	/**
	 * 设置标题栏右侧图标 id
	 */
	open fun getRightIcon(): Int? = null
	
	/**
	 * 设置标题栏右侧文字
	 */
	open fun getRightStr(): String? = null
	
	/**
	 * 设置标题栏文字
	 */
	open fun getTitleContent(): String = ""
	
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
	
	override fun initEvent() {
		super.initEvent()
		emptyHolderView.llEmpty.singleClick(this)
		titleBind.igRight.singleClick(this)
		titleBind.tvRight.singleClick(this)
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
			R.id.ll_empty -> callRefreshView()
			R.id.ig_right -> rightIconClick()
			R.id.tv_right -> rightTvClick()
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