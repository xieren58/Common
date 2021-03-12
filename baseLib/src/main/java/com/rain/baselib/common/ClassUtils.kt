package com.rain.baselib.common

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.*


/**
 *  Create by rain
 *  Date: 2020/12/1
 */
@Suppress("UNCHECKED_CAST")
fun <T : ViewBinding> AppCompatActivity.conversionViewBind(): T {
	val aClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
	Log.d("superclassTag", "aClass:$aClass")
	val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
	Log.d("superclassTag", "method:$method")
	return method.invoke(null, layoutInflater) as T
}

@Suppress("UNCHECKED_CAST")
fun <T : ViewBinding> Fragment.conversionViewBind(inflater: LayoutInflater, container: ViewGroup?): T {
	val aClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
	Log.d("superclassTag", "fg-aClass:$aClass")
	val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
	Log.d("superclassTag", "fg-method:$method")
	return method.invoke(null, inflater, container, false) as T
}

fun ViewGroup.getBindIdView(@LayoutRes layoutResId: Int): View {
	return DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(context), layoutResId, this, false).root
}

fun <VB : ViewDataBinding> ViewGroup.getBind(@LayoutRes layoutResId: Int): VB = DataBindingUtil.inflate(LayoutInflater.from(context), layoutResId, this, false)

@Suppress("UNCHECKED_CAST")
fun <T : ViewModel> AppCompatActivity.conversionViewModel(): T {
	val aClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<T>
	Log.d("superclassTag", "aClass:$aClass")
	return ViewModelProvider(this).get(aClass)
}

@Suppress("UNCHECKED_CAST")
fun <T : ViewModel> Fragment.conversionViewModel(): T {
	val aClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<T>
	Log.d("superclassTag", "aClass:$aClass")
	return ViewModelProvider(this).get(aClass)
}

