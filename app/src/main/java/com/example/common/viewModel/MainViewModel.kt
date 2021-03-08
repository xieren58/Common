package com.example.common.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.common.http.RetrofitFac
import com.example.common.http.scope.launchNoUI
import com.example.common.http.scope.launchUI
import com.rain.baselib.viewModel.BaseViewModel

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class MainViewModel : BaseViewModel() {
	
	fun testLaunchLine() {
		Log.d("testLaunchTag", "----------------------------------testLaunchLine---------------------------------------------")
		val startTestTime = System.currentTimeMillis()
		Log.d("testLaunchTag", "startTime-Line-$startTestTime")
		viewModelScope.launchUI({
			
			val suspendTime = System.currentTimeMillis()
			Log.d("testLaunchTag", "suspend-Line-${suspendTime},time:${suspendTime - startTestTime}")
			RetrofitFac.iData.getTeachHomeList(null)
		}, {
			val successTime = System.currentTimeMillis()
			Log.d("testLaunchTag", "success-Line-$successTime,time:${successTime - startTestTime}")
		}, {
			val failTime = System.currentTimeMillis()
			Log.d("testLaunchTag", "fail-Line-$failTime,,time:${failTime - startTestTime}")
		})
	}
	
	fun testLaunchNoLine() {
		Log.d("testLaunchTag", "----------------------------------testLaunchNoLine---------------------------------------------")
		val startTestTime = System.currentTimeMillis()
		Log.d("testLaunchTag", "startTime-noLine-$startTestTime")
		viewModelScope.launchNoUI({
			val suspendTime = System.currentTimeMillis()
			Log.d("testLaunchTag", "suspend-noLine-${suspendTime},time:${suspendTime - startTestTime}")
			RetrofitFac.iData.getTeachHomeList(null)
		}, {
			val successTime = System.currentTimeMillis()
			Log.d("testLaunchTag", "success-noLine-$successTime,time:${successTime - startTestTime}")
		}, {
			val failTime = System.currentTimeMillis()
			Log.d("testLaunchTag", "fail-noLine-$failTime,,time:${failTime - startTestTime}")
		})
	}
}