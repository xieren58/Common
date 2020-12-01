package com.rain.baselib.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
	
	open fun setData(model: T, position: Int) {
		if (variableId != -1) {
			dataBind?.setVariable(variableId, model)
			dataBind?.executePendingBindings()
		}
	}
}