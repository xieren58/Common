package com.says.common.ui

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
object UICommon {
	
	/**
	 * dp转px
	 */
	fun dip2px(context: Context,dpValue: Float): Int {
		val scale = context.resources.displayMetrics.density
		return (dpValue * scale + 0.5f).toInt()
	}
	
	/**
	 * sp转px
	 */
	fun sp2px(context: Context,sp: Float): Int {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics).toInt()
	}
	
	

	
	/**
	 * 关闭输入法
	 */
	fun closeMethod(activity: Activity) {
		try {
			val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
		} catch (e: java.lang.Exception) {
		}
	}
}