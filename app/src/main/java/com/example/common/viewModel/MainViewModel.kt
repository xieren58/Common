package com.example.common.viewModel

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.common.http.BasePagingSource
import com.example.common.http.RetrofitFac
import com.example.common.http.scope.*
import com.example.common.model.TeachModel
import com.rain.baselib.viewModel.BaseViewModel
import com.says.common.utils.DataStoreCommon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class MainViewModel : BaseViewModel() {
	fun pagerTest() = Pager(PagingConfig(pageSize = 10)) {
		return@Pager object : BasePagingSource<TeachModel>() {
			override fun loadIndex(): Int? = null
			override suspend fun loadDataRetrofit(index: Int?): MutableList<TeachModel>? {
				val data = RetrofitFac.iData.getTeachHomeList(index).resultData()
				Log.d("testLaunchTag", "startTime-data =-$data")
				return data?.sufferingList
			}
		}
	}.flow.cachedIn(viewModelScope)
	
	fun testBreak() {
		viewModelScope.launch {
			Log.d("testBreakTag", "string:${DataStoreCommon.getString("demo")}")
			DataStoreCommon.putValue("demo" to "test")
			Log.d("testBreakTag", "string1:${DataStoreCommon.getString("demo")}")
		}
	}
	
	fun testLaunchNoLine() {
		
		Log.d(
				"testLaunchTag",
				"----------------------------------testLaunchNoLine---------------------------------------------"
		)
		val startTestTime = System.currentTimeMillis()
		Log.d("testLaunchTag", "startTime-noLine-$startTestTime")
		viewModelScope.launchNoUI({
			val suspendTime = System.currentTimeMillis()
			Log.d(
					"testLaunchTag",
					"suspend-noLine-${suspendTime},time:${suspendTime - startTestTime}"
			)
			RetrofitFac.iData.getTeachHomeList(null)
		}, {
			val successTime = System.currentTimeMillis()
			Log.d(
					"testLaunchTag",
					"success-noLine-$successTime,time:${successTime - startTestTime}"
			)
		}, {
			val failTime = System.currentTimeMillis()
			Log.d("testLaunchTag", "fail-noLine-$failTime,,time:${failTime - startTestTime}")
		})
	}
}