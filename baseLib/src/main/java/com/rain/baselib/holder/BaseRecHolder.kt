package com.rain.baselib.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
abstract class BaseRecHolder<T> private constructor(override val containerView: View, val mContext: Context = containerView.context) : RecyclerView.ViewHolder(containerView), LayoutContainer {
	abstract val variableId: Int
	
	constructor(@LayoutRes layoutResId: Int, parent: ViewGroup) : this(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), layoutResId, parent, false).root)
	
	open fun setData(model: T, position: Int) {
		val dataBind = DataBindingUtil.getBinding<ViewDataBinding>(itemView)
		dataBind?.setVariable(variableId, model)
	}
}