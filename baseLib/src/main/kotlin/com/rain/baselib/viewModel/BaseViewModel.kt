package com.rain.baselib.viewModel

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.rain.baselib.liveData.ProtectedUnPeekLiveData
import com.rain.baselib.liveData.UnPeekLiveData

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
open class BaseViewModel : ViewModel() {
    open fun initModel() = Unit
    private val loadDialogType by lazy { UnPeekLiveData<Boolean>() }

    /**
     * 显示load
     */
    fun showLoadDialog() {
        loadDialogType.postValue(true)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("viewModelTag", "onCleared")
    }

    /**
     * 隐藏load
     */
    fun dismissDialog() {
        loadDialogType.postValue(false)
    }

    fun setLoadDialogObserve(activity: AppCompatActivity, observer: Observer<Boolean?>) {
        loadDialogType.observeInActivity(activity, observer)
    }

    fun setLoadDialogObserve(fragment: Fragment, observer: Observer<Boolean?>) {
        loadDialogType.observeInFragment(fragment, observer)
    }
}