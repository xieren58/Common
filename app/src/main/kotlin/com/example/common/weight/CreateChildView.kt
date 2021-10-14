package com.example.common.weight

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View

/**
 *  Create by rain
 *  Date: 2020-02-22
 *  生成控件类
 */
object CreateChildView {
    
    fun getChildView(context: Context, isGroup: Boolean): View {
        val view = SocialItemShareView(context)
        view.setTitle("哈哈哈哈")
        view.addFgView(TestViewGroup(context))
        Log.d("testTag", "view:$view")
        return view
    }
    
}