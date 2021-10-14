@file:Suppress("DEPRECATION")

package com.says.common.signature.listener

import android.os.Build
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener

/**
 *  Create by rain
 *  Date: 2021/4/2
 */
object ViewTreeObserverCompat {
    @JvmStatic
    fun removeOnGlobalLayoutListener(observer: ViewTreeObserver, victim: OnGlobalLayoutListener?) {
        // Future (API16+)...
        if (Build.VERSION.SDK_INT >= 16) {
            observer.removeOnGlobalLayoutListener(victim)
        } else {
            observer.removeGlobalOnLayoutListener(victim)
        }
    }
}