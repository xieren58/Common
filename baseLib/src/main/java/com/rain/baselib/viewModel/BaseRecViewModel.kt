package com.rain.baselib.viewModel

import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import com.rain.baselib.adapter.BaseRecAdapter
import com.rain.baselib.common.NetWorkUtils

/**
 *  Create by rain
 *  Date: 2020/11/9
 */
abstract class BaseRecViewModel<T> : BaseViewModel() {
    companion object {
        const val UI_TYPE_NO = 0
        const val UI_TYPE_LOAD = 1
        const val UI_TYPE_DATA = 2
        const val UI_TYPE_EMPTY = 3
        const val UI_TYPE_ERROR = 4
    }

    val uiDataType = MutableLiveData(UI_TYPE_NO)
    val loadEnd = MutableLiveData(true)
    abstract val adapter: BaseRecAdapter<T>
    protected var pageIndex = 1
    private var isMoreLoadEnable = true


    fun setMoreLoadEnable(isMoreLoadEnable: Boolean) {
        this.isMoreLoadEnable = isMoreLoadEnable
    }

    open fun loadSuccess(list: MutableList<T>?) {
        if (!list.isNullOrEmpty()) {
            if (pageIndex > 1) adapter.addItemData(list) else adapter.setData(list)
            pageIndex++
        }
        loadEnd.value = true
        showDataType()
    }

    open val isOpenFirstLoad = true
    fun showDataType() {
        uiDataType.value = if (adapter.getLists().isNullOrEmpty()) UI_TYPE_EMPTY else UI_TYPE_DATA
    }

    fun loadFail() {
        loadEnd.value = true
        showDataType()
    }

    @CallSuper
    override fun initModel() {
        if (isOpenFirstLoad) loadStartData(isRefresh = true, isShowLoad = true)
    }


    fun isShowDataView() = uiDataType.value != null && uiDataType.value == UI_TYPE_DATA

    /**
     * 重置拉取坐标
     */
    open fun resetPageIndex() {
        pageIndex = 1
    }

    fun loadStartData(isRefresh: Boolean, isShowLoad: Boolean) {
        val isLoad = loadEnd.value
        if (isLoad != null && !isLoad) return
        loadEnd.value = false
        if (isRefresh) {
            resetPageIndex()
            if (isShowLoad) uiDataType.value = UI_TYPE_LOAD
        }
        if (!NetWorkUtils.isNetConnected()) {
            loadEnd.value = true
            uiDataType.value = UI_TYPE_ERROR
            return
        }
        loadData()
    }

    fun getLists() = adapter.getLists()
    fun getItemData(position: Int) = adapter.getItemData(position)

    protected abstract fun loadData()
}