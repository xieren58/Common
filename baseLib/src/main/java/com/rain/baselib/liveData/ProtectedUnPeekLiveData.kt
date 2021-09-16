package com.rain.baselib.liveData

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.*

/**
 *  Create by rain
 *  Date: 2021/3/12
 */
open class ProtectedUnPeekLiveData<T> : LiveData<T>() {
    protected var isAllowNullValue = false
    private val observers = HashMap<Int, Boolean>()
    fun observeInActivity(activity: AppCompatActivity, observer: Observer<in T>) {
        val owner: LifecycleOwner = activity
        val storeId = System.identityHashCode(activity.viewModelStore)
        observe(storeId, owner, observer)
    }

    fun observeInFragment(fragment: Fragment, observer: Observer<in T?>) {
        val owner = fragment.viewLifecycleOwner
        val storeId = System.identityHashCode(fragment.viewModelStore)
        observe(storeId, owner, observer)
    }

    private fun observe(storeId: Int, owner: LifecycleOwner, observer: Observer<in T>) {
        if (observers[storeId] == null) {
            observers[storeId] = true
        }
        super.observe(owner, { t: T ->
            if (observers[storeId] != true) {
                observers[storeId] = true
                if (t != null || isAllowNullValue) {
                    observer.onChanged(t)
                }
            }
        })
    }

    override fun setValue(value: T?) {
        if (value != null || isAllowNullValue) {
            for (entry in observers.entries) {
                entry.setValue(false)
            }
            super.setValue(value)
        }
    }

    protected fun clear() {
        super.setValue(null)
    }
}