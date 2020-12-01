package com.rain.baselib.viewModel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
abstract class BaseViewModel :ViewModel(){
    open fun initModel() = Unit
    private val loadDialogType = MutableLiveData<Boolean>()
    /**
     * 显示load
     */
    fun showLoadDialog() {
        loadDialogType.value = true
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("viewModelTag","onCleared")
    }
    /**
     * 隐藏load
     */
    fun dismissDialog() {
        loadDialogType.value = false
    }

    fun setLoadDialogObserve(owner: LifecycleOwner, observer: Observer<Boolean?>) {
        loadDialogType.observe(owner, observer)
    }

}