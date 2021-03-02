package com.rain.baselib.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
open class BaseRecHolder<T, DB : ViewDataBinding>(containerView: View, val variableId: Int = -1) : RecyclerView.ViewHolder(containerView) {
	protected val dataBind by lazy { DataBindingUtil.getBinding<DB>(itemView) }
	protected val mContext: Context = itemView.context
	
	/**
	 * holder刷新当前item的所有数据
	 * 由adapter.onBindViewHolder(RecyclerView.ViewHolder holder, int position)回調
	 */
	open fun setData(model: T, position: Int) {
		if (variableId != -1) {
			dataBind?.setVariable(variableId, model)
			dataBind?.executePendingBindings()
		}
	}
	
	/**
	 * holder局部刷新，只更新改变了的数据，payloads为刷新设置的标记
	 * 由adapter.onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads)回調
	 */
	open fun updateData(model: T, position: Int, payloads: Any) {
		setData(model, position)
	}
}