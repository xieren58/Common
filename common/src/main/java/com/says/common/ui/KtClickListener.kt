package com.says.common.ui

import android.view.View
import android.widget.Checkable

/**
 *  Create by rain
 *  Date: 2020/3/12
 */
inline fun <T : View> T.singleClick(time: Long = 300, crossinline block: (T) -> Unit) {
	setOnClickListener {
		val currentTimeMillis = System.currentTimeMillis()
		if (currentTimeMillis - lastClickTime > time || this is Checkable) {
			lastClickTime = currentTimeMillis
			block(this)
		}
	}
}
//兼容点击事件设置为this的情况
fun <T : View> T.singleClick(onClickListener: View.OnClickListener, time: Long = 300) {
	setOnClickListener {
		val currentTimeMillis = System.currentTimeMillis()
		if (currentTimeMillis - lastClickTime > time || this is Checkable) {
			lastClickTime = currentTimeMillis
			onClickListener.onClick(this)
		}
	}
}

var <T : View> T.lastClickTime: Long
	set(value) = setTag(1766613352, value)
	get() = getTag(1766613352) as? Long ?: 0