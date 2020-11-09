package com.rain.baselib.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.rain.baselib.BaseLibContext

/**
 *  Create by rain
 *  Date: 2020/11/9
 */
object NetWorkUtils {
	/**
	 * 判断WIFI网络是否可用
	 */
	@Suppress("DEPRECATION")
	fun isNetConnected(): Boolean {
		val connectivityManager = BaseLibContext.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		if (Build.VERSION.SDK_INT >= 29) {
			val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
					?: return false
			return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
		} else {
			val activeNetworkInfo = connectivityManager.activeNetworkInfo
			return activeNetworkInfo != null && activeNetworkInfo.isConnected
		}
	}
	
}