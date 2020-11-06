package com.rain.baselib.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.rain.baselib.common.singleClick
import com.rain.baselib.holder.BaseRecHolder

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
abstract class BaseRecAdapter<T> : RecyclerView.Adapter<BaseRecHolder<T>>(), View.OnClickListener, View.OnLongClickListener {
	private val lists: MutableList<T> = mutableListOf()
	private var recycler: RecyclerView? = null
	override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
		super.onAttachedToRecyclerView(recyclerView)
		this.recycler = recyclerView
	}
	
	open fun setData(list: MutableList<T>?) {
		if (list.isNullOrEmpty() || recycler?.isComputingLayout == true) return
		lists.clear()
		lists.addAll(list)
		notifyDataSetChanged()
	}
	
	/**
	 * 获取集合对象
	 */
	fun getLists() = lists
	
	fun addItemData(data: MutableList<T>?) {
		if (data.isNullOrEmpty() || recycler?.isComputingLayout == true) return
		val position = lists.size
		lists.addAll(position, data)
		notifyItemRangeInserted(position, data.size)
		notifyItemRangeChanged(position, lists.size - position)
	}
	
	fun addItemData(position: Int, data: MutableList<T>?) {
		if (data.isNullOrEmpty() || recycler?.isComputingLayout == true) return
		lists.addAll(position, data)
		notifyItemRangeInserted(position, data.size)
		notifyItemRangeChanged(position, lists.size - position)
	}
	
	fun addItemData(data: T?) {
		if (data == null || recycler?.isComputingLayout == true) return
		val position = lists.size
		lists.add(position, data)
		notifyItemInserted(position)
		notifyItemRangeChanged(position, lists.size - position)
	}
	
	fun removeItemData(position: Int) {
		if (recycler?.isComputingLayout == true) return
		if (lists.isNullOrEmpty() || position < 0 || position >= lists.size) return
		lists.removeAt(position)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, lists.size - position)
	}
	
	fun removeItemData(data: T?) {
		if (recycler?.isComputingLayout == true) return
		if (lists.isNullOrEmpty() || data == null) return
		val position = lists.indexOf(data)
		lists.remove(data)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, lists.size - position)
	}
	
	
	fun getItemData(position: Int): T? {
		return if (position < lists.size && position >= 0) lists[position] else null
	}
	
	fun updateItemData(data: T?) {
		if (data == null || lists.isNullOrEmpty()) return
		val indexOf = lists.indexOf(data)
		if (indexOf >= 0) notifyItemChanged(indexOf)
	}
	
	override fun onClick(view: View) {
		val tag = view.tag ?: return
		if (recycler?.isComputingLayout == true) return
		val position = tag as Int
		itemClickListener?.itemClick(position)
	}
	
	/**
	 * item点击
	 */
	interface OnItemClickListener {
		fun itemClick(position: Int)
	}
	
	private var itemClickListener: OnItemClickListener? = null
	
	fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
		this.itemClickListener = itemClickListener
	}
	
	/**
	 * item长按
	 */
	interface OnItemLongClickListener {
		fun itemLongClick(position: Int)
	}
	
	
	private var onItemLongClickListener: OnItemLongClickListener? = null
	fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
		this.onItemLongClickListener = onItemLongClickListener
	}
	
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecHolder<T> {
		val viewHolder = getViewHolder(parent, viewType)
		viewHolder.itemView.singleClick(this)
		viewHolder.itemView.setOnLongClickListener(this)
		return viewHolder
	}
	
	override fun onBindViewHolder(holder: BaseRecHolder<T>, position: Int) {
		holder.itemView.tag = position
		holder.setData(lists[position], position)
		bindHolder(holder, position)
	}
	
	
	/**
	 * 利用DataBind创建view
	 */
	fun <T : ViewDataBinding> createViewId(@LayoutRes layoutId: Int, viewGroup: ViewGroup): View {
		return DataBindingUtil.inflate<T>(LayoutInflater.from(viewGroup.context), layoutId, viewGroup, false).root
	}
	
	
	override fun getItemCount() = lists.size
	
	abstract fun getViewHolder(viewGroup: ViewGroup, viewType: Int): BaseRecHolder<T>
	
	
	open fun bindHolder(viewHolder: BaseRecHolder<T>, i: Int) {}
	override fun onLongClick(v: View?): Boolean {
		val tag = v?.tag ?: return false
		val position = tag as Int
		onItemLongClickListener?.itemLongClick(position)
		return true
	}
	
}