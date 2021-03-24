package com.example.common.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.common.http.BasePagingSource
import com.example.common.http.RetrofitFac
import com.example.common.http.scope.*
import com.example.common.model.TeachModel
import com.rain.baselib.viewModel.BaseViewModel
import com.says.common.dataStore.DataStoreCommon
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
		viewModelScope.launch {
			DataStoreCommon.clear()
		}
	}
}