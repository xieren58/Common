package com.rain.baselib.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.rain.baselib.common.getBind
import com.rain.baselib.common.singleClick
import com.rain.baselib.holder.BaseRecHolder

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
abstract class BaseRecAdapter<T> : RecyclerView.Adapter<BaseRecHolder<T, *>>(), View.OnClickListener, View.OnLongClickListener {
	private val adapterList: MutableList<T> = mutableListOf()
	private var recycler: RecyclerView? = null
	
	/**
	 * 获取所依赖RecyclerView对象，判断是否正在绘制中
	 */
	override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
		super.onAttachedToRecyclerView(recyclerView)
		this.recycler = recyclerView
	}
	
	/**
	 * 设置数据源
	 */
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
	
	/**
	 * 添加数据
	 */
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
	
	/**
	 * 删除某条数据
	 */
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
	
	/**
	 * 更新指定数据
	 * payloads -指定更新某一字段
	 */
	fun updateItemData(data: T?) {
		if (data == null || adapterList.isNullOrEmpty()) return
		val indexOf = adapterList.indexOf(data)
		if (indexOf >= 0) notifyItemChanged(indexOf)
	}
	
	fun updateItemData(data: T?, payloads: Any) {
		if (data == null || adapterList.isNullOrEmpty()) return
		val indexOf = adapterList.indexOf(data)
		if (indexOf >= 0) notifyItemChanged(indexOf, payloads)
	}
	
	override fun onClick(view: View) {
		val tag = view.tag ?: return
		if (recycler?.isComputingLayout == true) return
		val position = tag as? Int ?: return
		itemClickListener?.invoke(position)
	}
	
	/**
	 * item点击
	 */
	interface OnItemClickListener {
		fun itemClick(position: Int)
	}
	
	/**
	 * 点击事件回调(Lambda表达式方式 )
	 */
	private var itemClickListener: ((position: Int) -> Unit)? = null
	
	fun setOnItemClickListener(itemClickListener: ((position: Int) -> Unit)?) {
		this.itemClickListener = itemClickListener
	}
	
	/**
	 * item长按
	 */
	interface OnItemLongClickListener {
		fun itemLongClick(position: Int)
	}
	
	/**
	 * 长按事件回调(Lambda表达式方式 )
	 */
	private var onItemLongClickListener: ((position: Int) -> Unit)? = null
	fun setOnItemLongClickListener(onItemLongClickListener: ((position: Int) -> Unit)) {
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
		val model = adapterList[position]
		holder.setData(model, position)
		collectHolder(holder, model, position)
	}
	
	/**
	 * 更新数据处理默认由holder内部setData方法处理
	 */
	open fun collectHolder(holder: BaseRecHolder<T, *>, model: T, position: Int) {}
	override fun onBindViewHolder(holder: BaseRecHolder<T, *>, position: Int, payloads: MutableList<Any>) {
		if (payloads.isEmpty()) {
			onBindViewHolder(holder, position)
			return
		}
		holder.updateData(adapterList[position], position, payloads[0])
	}
	
	override fun getItemCount() = adapterList.size
	
	private fun createHolder(parent: ViewGroup, viewType: Int): BaseRecHolder<T, *> {
		return createHolder(parent, viewType, getLayoutResId(viewType), getVariableId(viewType))
	}
	
	/**
	 * 创建holder，默认使用holder
	 */
	open fun createHolder(parent: ViewGroup, viewType: Int, @LayoutRes layoutResId: Int, variableId: Int): BaseRecHolder<T, *> {
		return BaseRecHolder(parent.getBind(layoutResId), variableId)
	}
	
	/**
	 * 获取布局id
	 */
	@LayoutRes
	abstract fun getLayoutResId(viewType: Int): Int
	
	/**
	 * 获取绑定布局的数据id
	 */
	abstract fun getVariableId(viewType: Int): Int //綁定的id 為-1時表示不綁定
	
	override fun onLongClick(v: View?): Boolean {
		val tag = v?.tag ?: return false
		val position = tag as? Int ?: return false
		onItemLongClickListener?.invoke(position)
		return true
	}
}