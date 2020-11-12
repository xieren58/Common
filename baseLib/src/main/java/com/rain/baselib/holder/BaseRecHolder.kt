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
abstract class BaseRecHolder<T>(@LayoutRes layoutResId: Int, parent: ViewGroup, private var variableId: Int = -1) : RecyclerView.ViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), layoutResId, parent, false).root), LayoutContainer {
	override val containerView: View = itemView
	protected val mContext:Context = itemView.context
	open fun setData(model: T, position: Int) {
		if (variableId != -1) {
			val dataBind = DataBindingUtil.getBinding<ViewDataBinding>(itemView)
			dataBind?.setVariable(variableId, model)
			dataBind?.executePendingBindings()
		}
	}
}