package com.rain.baselib.activity

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.rain.baselib.R
import com.rain.baselib.common.singleClick
import com.rain.baselib.viewModel.BaseRecViewModel
import com.rain.baselib.weight.ScrollOtherRefreshLayout

/**
 *  RecyclerView的基类
 *  在默认的布局 [R.layout.activity_base_rec] 下传入[ActivityBaseRecBinding]
 *  重写 [layoutResId] 必须设置相对的viewDataBinding
 */
abstract class BaseRecActivity : BaseDataBindActivity<ViewDataBinding>(), View.OnClickListener {
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
	private val loadingView: View? by lazy { findViewById(R.id.rl_loading) }
	
	/**
	 * 数据集合布局
	 */
	private val recData: RecyclerView? by lazy { findViewById(R.id.rv_data) }
	private val smartRefresh: ScrollOtherRefreshLayout? by lazy { findViewById(R.id.smart_refresh) }
	
	/**
	 * 错误布局
	 */
	private val errView: View? by lazy { findViewById(R.id.rl_error) }
	
	/**
	 * 空布局
	 */
	private val emptyView: View? by lazy { findViewById(R.id.rl_empty) }
	private val emptyClickView: View? by lazy { findViewById(R.id.ll_empty) }
	
	/**
	 * 标题栏布局
	 */
	private val toolbar: Toolbar? by lazy { findViewById(R.id.toolbar) }
	private val tvTitle: TextView? by lazy { findViewById(R.id.tv_title) }
	private val igRight: ImageView? by lazy { findViewById(R.id.ig_right) }
	private val tvRight: TextView? by lazy { findViewById(R.id.tv_right) }
	
	override fun initModelObserve() {
		viewModel.loadEnd.observe(this, {
			if (it == null || it) {
				recData?.isNestedScrollingEnabled = true
				finishLoad()
			} else recData?.isNestedScrollingEnabled = false
		})
		viewModel.uiDataType.observe(this, {
			if (it != null) showUi(it)
		})
	}
	
	/**
	 * 设置展示的view
	 */
	private fun showUi(value: Int) {
		loadingView?.visibility = if (value == BaseRecViewModel.UI_TYPE_LOAD) View.VISIBLE else View.GONE
		emptyView?.visibility = if (value == BaseRecViewModel.UI_TYPE_EMPTY) View.VISIBLE else View.GONE
		errView?.visibility = if (value == BaseRecViewModel.UI_TYPE_ERROR) View.VISIBLE else View.GONE
		smartRefresh?.visibility = if (value == BaseRecViewModel.UI_TYPE_DATA) View.VISIBLE else View.GONE
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
		recData?.layoutManager = getRecLayoutManager()
		recItemDecoration?.run {
			recData?.removeItemDecoration(this)
			recData?.addItemDecoration(this)
		}
		viewModel.adapter.run {
			setOnItemClickListener {
				clickRecItem(it)
			}
			setOnItemLongClickListener {
				itemLong(it)
			}
			recData?.adapter = this
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
		smartRefresh?.bindRecycler(recData)
		smartRefresh?.setLoadRefreshMoreDataListener({
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
		tvTitle?.text = titleContent ?: ""
		toolbar?.run {
			setNavigationIcon(R.drawable.arrow_left)
			title = ""
			setSupportActionBar(this)
			setNavigationOnClickListener { finish() }
		}
		
		igRight?.visibility = rightIcon?.run {
			igRight?.setImageResource(this)
			View.VISIBLE
		} ?: View.GONE
		
		tvRight?.visibility = if (rightStr.isNullOrEmpty()) View.GONE
		else {
			tvRight?.text = rightStr ?: ""
			View.VISIBLE
		}
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
	open val titleContent: String? = null
	
	/**
	 * 设置下拉刷新和上拉加载开关
	 */
	private fun setMoreRefreshEnable() {
		smartRefresh?.setEnableRefresh(loadRefreshEnable)//启用刷新
		smartRefresh?.setEnableLoadMore(loadMoreEnable)//启用加载
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
		igRight?.singleClick(this)
		tvRight?.singleClick(this)
	}
	
	/**
	 * 提供子类调用刷新页面，重新请求数据的方法
	 */
	fun callRefreshView() {
		if (recData?.isComputingLayout == true) return
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