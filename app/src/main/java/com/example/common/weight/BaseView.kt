package com.example.common.weight

import android.content.Context
import android.util.Log
import android.widget.FrameLayout

/**
 *  Create by rain
 *  Date: 2021/2/26
 */
abstract class BaseView (context: Context): FrameLayout(context)  {

    init {
        initBase()
    }
    private fun initBase(){
        Log.d("testTag","viewBase")
    }
}