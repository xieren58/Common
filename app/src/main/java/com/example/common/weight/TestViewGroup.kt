package com.example.common.weight

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import com.example.common.adapter.ChildShowAdapter
import com.rain.baselib.adapter.BaseRecAdapter

/**
 *  Create by rain
 *  Date: 2021/2/26
 */
class TestViewGroup(context: Context) : BaseView(context) {
	private val adapter by lazy { "test" }
	
	init {
		addView(TextView(context).apply {
			text = "测试"
			setTextColor(Color.parseColor("#666666"))
			textSize = 27F
		})
		Log.d("testTag", "adapter:$adapter")
	}
}