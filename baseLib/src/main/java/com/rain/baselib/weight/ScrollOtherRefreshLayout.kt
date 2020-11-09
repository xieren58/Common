package com.rain.baselib.weight

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import java.lang.ref.WeakReference
import kotlin.properties.Delegates

/**
 *  Create by rain
 *  Date: 2020/10/26
 */
class ScrollOtherRefreshLayout constructor(context: Context?, attrs: AttributeSet?) : SmartRefreshLayout(context, attrs) {
	
	constructor(context: Context?) : this(context, null)
	
	private var recyclerChild: WeakReference<RecyclerView> by Delegates.notNull()
	fun bindRecycler(recycler: RecyclerView) {
		recyclerChild = WeakReference(recycler)
	}
	
	fun setRefreshDataListener(block: (refreshLayout: RefreshLayout) -> Unit) {
		setOnRefreshListener {
			val recycler = recyclerChild.get()
			if (recycler?.isComputingLayout == true) {
				finishRefresh()
				return@setOnRefreshListener
			}
			block(it)
		}
	}
	
	fun setLoadMoreDataListener(block: (refreshLayout: RefreshLayout) -> Unit) {
		setOnLoadMoreListener {
			val recycler = recyclerChild.get()
			if (recycler?.isComputingLayout == true) {
				finishLoadMore()
				return@setOnLoadMoreListener
			}
			block(it)
		}
	}
	
	fun setLoadRefreshMoreDataListener(refreshBlock: (refreshLayout: RefreshLayout) -> Unit, loadMoreBlock: (refreshLayout: RefreshLayout) -> Unit) {
		setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
			override fun onLoadMore(refreshLayout: RefreshLayout) {
				val recycler = recyclerChild.get()
				if (recycler?.isComputingLayout == true) {
					finishLoadMore()
					return
				}
				loadMoreBlock(refreshLayout)
			}
			
			override fun onRefresh(refreshLayout: RefreshLayout) {
				val recycler = recyclerChild.get()
				if (recycler?.isComputingLayout == true) {
					finishRefresh()
					return
				}
				refreshBlock(refreshLayout)
			}
		})
	}
	
}