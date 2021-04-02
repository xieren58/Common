package com.example.common.weight

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 *  Create by rain
 *  Date: 2021/4/2
 */
class RotateTextView @JvmOverloads constructor(context: Context, attr: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attr, defStyleAttr) {
	override fun onDraw(canvas: Canvas?) {
		canvas?.rotate(90F, (measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat())
		super.onDraw(canvas)
	}
}
