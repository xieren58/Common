package com.rain.baselib.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.rain.baselib.common.getBindIdView
import com.rain.baselib.common.singleClick
import com.rain.baselib.holder.BaseRecHolder

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
abstract class BaseRecAdapter<T> : RecyclerView.Adapter<BaseRecHolder<T, *>>(), View.OnClickListener, View.OnLongClickListener {
	private val adapterList: MutableList<T> = mutableListOf()
	private var recycler: RecyclerView? = null
	override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
		super.onAttachedToRecyclerView(recyclerView)
		this.recycler = recyclerView
	}
	
	open fun setData(list: MutableList<T>?) {
		if (list.isNullOrEmpty() || recycler?.isComputingLayout == true) return
		adapterList.clear()
		adapterList.addAll(list)
		notifyDataSetChanged()
	}
	
	/**
	 * 获取集合对象
	 */
	fun getLists() = adapterList
	
	fun addItemData(data: MutableList<T>?) {
		if (data.isNullOrEmpty() || recycler?.isComputingLayout == true) return
		val position = adapterList.size
		addItemData(position, data)
	}
	
	fun addItemData(position: Int, data: MutableList<T>?) {
		if (data.isNullOrEmpty() || recycler?.isComputingLayout == true) return
		adapterList.addAll(position, data)
		notifyItemRangeInserted(position, data.size)
		notifyItemRangeChanged(position, adapterList.size - position)
	}
	
	fun addItemData(data: T?) {
		if (data == null || recycler?.isComputingLayout == true) return
		val position = adapterList.size
		addItemData(position, data)
	}
	
	fun addItemData(position: Int, data: T?) {
		if (data == null || recycler?.isComputingLayout == true) return
		adapterList.add(position, data)
		notifyItemInserted(position)
		notifyItemRangeChanged(position, adapterList.size - position)
	}
	
	fun removeItemData(position: Int) {
		if (recycler?.isComputingLayout == true) return
		if (adapterList.isNullOrEmpty() || position < 0 || position >= adapterList.size) return
		adapterList.removeAt(position)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, adapterList.size - position)
	}
	
	fun removeItemData(data: T?) {
		if (recycler?.isComputingLayout == true) return
		if (adapterList.isNullOrEmpty() || data == null) return
		val position = adapterList.indexOf(data)
		adapterList.remove(data)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, adapterList.size - position)
	}
	
	
	fun getItemData(position: Int): T? {
		return if (position < adapterList.size && position >= 0) adapterList[position] else null
	}
	
	fun updateItemData(data: T?) {
		if (data == null || adapterList.isNullOrEmpty()) return
		val indexOf = adapterList.indexOf(data)
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
	
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecHolder<T, *> {
		val holder = createHolder(parent, viewType)
		holder.itemView.singleClick(this)
		holder.itemView.setOnLongClickListener(this)
		return holder
	}
	
	override fun onBindViewHolder(holder: BaseRecHolder<T, *>, position: Int) {
		holder.itemView.tag = position
		holder.setData(adapterList[position], position)
		bindHolder(holder, position)
	}
	
	override fun getItemCount() = adapterList.size
	
	private fun createHolder(parent: ViewGroup, viewType: Int): BaseRecHolder<T, *> {
		return createHolder(parent.getBindIdView(getLayoutResId(viewType)), viewType, getVariableId(viewType))
	}
	
	@LayoutRes
	abstract fun getLayoutResId(viewType: Int): Int
	abstract fun getVariableId(viewType: Int): Int //綁定的id 為-1時表示不綁定
	
	open fun createHolder(view: View, viewType: Int, variableId: Int): BaseRecHolder<T, *> {
		return BaseRecHolder<T, ViewDataBinding>(view, variableId)
	}
	
	
	open fun bindHolder(viewHolder: BaseRecHolder<T, *>, i: Int) {}
	override fun onLongClick(v: View?): Boolean {
		val tag = v?.tag ?: return false
		val position = tag as Int
		onItemLongClickListener?.itemLongClick(position)
		return true
	}
}