package com.example.common.fragment

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 *  Create by rain
 *  Date: 2021/8/31
 */
class DemoTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
//        if (position >= -1.0f && position <= 0.0f) {
//            page.x = 1 + position * 0.1f
//            page.scaleX = 1 + position * 0.1f
//            page.scaleY = 1 + position * 0.2f
//        } else if (position > 0.0f && position < 1.0f) {
//            page.x = 1 - position * 0.1f
//            page.scaleX = 1 - position * 0.1f
//            page.scaleY = 1 - position * 0.2f
//        } else {
//            page.x = 1f
//            page.scaleX = 0.9f
//            page.scaleY = 0.8f
//        }
    }
}