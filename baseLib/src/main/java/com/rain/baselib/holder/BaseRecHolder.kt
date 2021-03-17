package com.rain.baselib.holder

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
open class BaseRecHolder<T, DB : ViewDataBinding>(val dataBind: DB, val variableId: Int = -1) :
    RecyclerView.ViewHolder(dataBind.root) {
    protected val mContext: Context = itemView.context

    /**
     * holder刷新当前item的所有数据
     * 由adapter.onBindViewHolder(RecyclerView.ViewHolder holder, int position)回調
     */
    open fun setData(model: T, position: Int) {
        initBindModel(model)
    }

    /**
     * holder局部刷新，只更新改变了的数据，payloads为刷新设置的标记
     * 由adapter.onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads)回調
     */
    open fun updateData(model: T, position: Int, payloads: Any) {
        initBindModel(model)
    }

    /**
     * dataBind更新数据方法
     */
    private fun initBindModel(model: T) {
        if (variableId != -1) {
            dataBind.setVariable(variableId, model)
            dataBind.executePendingBindings()
        }
    }
}