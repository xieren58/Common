package com.example.common.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.example.common.R
import com.example.common.databinding.ViewSocialShareBinding

/**
 *  Create by rain
 *  Date: 2021/3/1
 */
class SocialItemShareView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    
    private val parent by lazy { DataBindingUtil.inflate<ViewSocialShareBinding>(LayoutInflater.from(context), R.layout.view_social_share, this, false) }
    
    init {
        addView(parent.root, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }
    
    fun setTitle(str: String) {
        parent.tvTest.text = str
    }
    
    fun addFgView(view: View) {
        parent.frameLayout.removeAllViews()
        parent.frameLayout.addView(view)
    }
}