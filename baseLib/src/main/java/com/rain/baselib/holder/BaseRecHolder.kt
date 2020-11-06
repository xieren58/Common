package com.rain.baselib.holder

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
abstract class BaseRecHolder<T>(override val containerView: View, private val variableId: Int, val mContext: Context = containerView.context) : RecyclerView.ViewHolder(containerView), LayoutContainer {
	
	open fun setData(model: T, position: Int) {
		val dataBind = DataBindingUtil.getBinding<ViewDataBinding>(itemView)
		dataBind?.setVariable(variableId, model)
	}
}