package com.rain.baselib.activity

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.rain.baselib.R
import com.rain.baselib.adapter.BaseRecAdapter
import com.rain.baselib.common.singleClick
import com.rain.baselib.viewModel.BaseRecViewModel
import kotlinx.android.synthetic.main.base_title.*
import kotlinx.android.synthetic.main.layout_data_view.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_err_view.*
import kotlinx.android.synthetic.main.layout_loading_view.*

/**
 *  Create by rain
 *  Date: 2020/11/9
 */
abstract class BaseRecActivity : BaseActivity(), View.OnClickListener {
	override val layoutResId = R.layout.activity_base_rec
	abstract override val viewModel: BaseRecViewModel<*>
	
	protected open val loadMoreEnable = true
	protected open val loadRefreshEnable = true
	
	
	override fun initModelObserve() {
		viewModel.loadEnd.observe(this, {
			if (it == null || it) {
				rv_data?.isNestedScrollingEnabled = true
				finishLoad()
			} else rv_data?.isNestedScrollingEnabled = false
		})
		viewModel.uiDataType.observe(this, {
			if (it != null) showUi(it)
		})
	}
	
	private fun showUi(value: Int) {
		rl_loading?.visibility =
				if (value == BaseRecViewModel.UI_TYPE_LOAD) View.VISIBLE else View.GONE
		rl_empty?.visibility =
				if (value == BaseRecViewModel.UI_TYPE_EMPTY) View.VISIBLE else View.GONE
		rl_error?.visibility =
				if (value == BaseRecViewModel.UI_TYPE_ERROR) View.VISIBLE else View.GONE
		smart_refresh?.visibility =
				if (value == BaseRecViewModel.UI_TYPE_DATA) View.VISIBLE else View.GONE
	}
	
	@CallSuper
	override fun initView() {
		initToolbar()
		initSmart()
		initRec()
	}
	
	private fun initRec() {
		rv_data?.layoutManager = getRecLayoutManager()
		val itemDecoration = getReItemDecoration()
		if (itemDecoration != null) {
			rv_data?.removeItemDecoration(itemDecoration)
			rv_data?.addItemDecoration(itemDecoration)
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
			rv_data?.adapter = this
		}
	}
	
	protected open fun getReItemDecoration(): RecyclerView.ItemDecoration? = null
	abstract fun getRecLayoutManager(): RecyclerView.LayoutManager
	
	private fun initSmart() {
		smart_refresh.bindRecycler(rv_data)
		smart_refresh?.setLoadRefreshMoreDataListener({
			viewModel.loadStartData(isRefresh = false, isShowLoad = false)
		}, {
			viewModel.loadStartData(true, isShowLoad = false)
		})
		setMoreRefreshEnable()
	}
	
	private fun initToolbar() {
		if (tv_title == null || toolbar == null) return
		
		tv_title?.text = getTitleContent()
		toolbar.setNavigationIcon(R.drawable.arrow_left)
		toolbar.title = ""
		setSupportActionBar(toolbar)
		toolbar.setNavigationOnClickListener { finish() }
		
		val rightIcon = getRightIcon()
		if (rightIcon == null) {
			ig_right?.visibility = View.GONE
		} else {
			ig_right?.setImageResource(rightIcon)
			ig_right?.visibility = View.VISIBLE
		}
		val rightStr = getRightStr()
		if (rightStr.isNullOrEmpty()) {
			tv_right?.visibility = View.GONE
		} else {
			tv_right?.text = rightStr
			tv_right?.visibility = View.VISIBLE
		}
	}
	
	open fun getRightIcon(): Int? = null
	open fun getRightStr(): String? = null
	open fun getTitleContent(): String = ""
	
	private fun setMoreRefreshEnable() {
		smart_refresh?.setEnableRefresh(loadRefreshEnable)//启用刷新
		smart_refresh?.setEnableLoadMore(loadMoreEnable)//启用加载
	}
	
	private fun finishLoad() {
		smart_refresh?.finishRefresh()
		smart_refresh?.finishLoadMore()
	}
	
	override fun initEvent() {
		super.initEvent()
		ll_empty?.singleClick(this)
		ig_right?.singleClick(this)
		tv_right?.singleClick(this)
	}
	
	fun callRefreshView() {
		if (rv_data?.isComputingLayout == true) return
		var isShowLoad = false
		if (viewModel.isShowDataView()) {
			if (loadRefreshEnable) {
				smart_refresh?.autoRefresh()
				return
			}
		} else isShowLoad = true
		viewModel.loadStartData(isRefresh = true, isShowLoad = isShowLoad)
	}
	
	//adapter-item-click
	abstract fun clickRecItem(position: Int)
	
	//长按事件
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