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
import androidx.viewbinding.ViewBinding
import java.lang.reflect.*


/**
 *  Create by rain
 *  Date: 2020/12/1
 */
@Suppress("UNCHECKED_CAST")
fun <T : ViewBinding> AppCompatActivity.conversionViewBind() : T? {
	try {
		val superclass = javaClass.genericSuperclass
		Log.d("superclassTag","superclass:$superclass")
		val aClass = (superclass as ParameterizedType).actualTypeArguments[0] as Class<*>
		Log.d("superclassTag","aClass:$aClass")
		val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
		Log.d("superclassTag","method:$method")
		return  method.invoke(null, layoutInflater) as T
	} catch (e: NoSuchMethodException) {
		e.printStackTrace()
	} catch (e: IllegalAccessException) {
		e.printStackTrace()
	} catch (e: InvocationTargetException) {
		e.printStackTrace()
	}
    return null
}

@Suppress("UNCHECKED_CAST")
fun <T : ViewBinding> Fragment.conversionViewBind(inflater: LayoutInflater, container: ViewGroup?) :T?{
	try {
		val superclass = javaClass.genericSuperclass
		val aClass = (superclass as ParameterizedType).actualTypeArguments[0] as Class<*>
		val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
		return  method.invoke(null, inflater, container, false) as T
	} catch (e: NoSuchMethodException) {
		e.printStackTrace()
	} catch (e: IllegalAccessException) {
		e.printStackTrace()
	} catch (e: InvocationTargetException) {
		e.printStackTrace()
	}
    return null
}
fun ViewGroup.getBindIdView(@LayoutRes layoutResId: Int): View {
    return DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(context), layoutResId, this, false).root
}