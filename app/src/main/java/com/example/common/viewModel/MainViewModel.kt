package com.example.common.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.common.http.BasePagingSource
import com.example.common.http.RetrofitFac
import com.example.common.http.scope.*
import com.example.common.model.CityModel
import com.example.common.model.TeachModel
import com.rain.baselib.viewModel.BaseViewModel
import com.says.common.dataStore.DataStoreCommon
import kotlinx.coroutines.*

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class MainViewModel : BaseViewModel() {
	val cityModel: CityModel = CityModel().apply {
		name = "哈哈"
	}
	
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
	
	private var testScope: CoroutineScope? = null
		get() {
			if (field == null) {
				field = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
			}
			return field
		}
	
	fun testBreak() {
		testScope?.launch {
//			try {
//				val data = RetrofitFac.iData.getColumnConfig("2f36f162-13ee-4edc-8eb4-ecb1c5a5beb7",
//						"",
//						"3f100134-2c74-4927-8581-6b69fee8dfb0",
//						"f7b6387f-42e3-4edd-b9e3-6753b75b500c",
//						"", "7440c795-18ce-475e-97c2-1ec3877335d3", "2", "1"
//				).resultData()
//				Log.d("testBreakTag", "data:${data == null}")
//			} catch (e: Exception) {
//				Log.d("testBreakTag", "e:$e")
//				e.printStackTrace()
//			}
			try {
				val testSuspend = testSuspend()
				Log.d("testBreakTag", "testSuspend:$testSuspend")
			} catch (e: Exception) {
				Log.d("testBreakTag", "e:$e")
			}
		}
	}
	
	fun testLaunchNoLine() {
		testScope?.cancel()
		testScope = null
//		viewModelScope.launch {
//			DataStoreCommon.clear()
//		}
	}
	
	private suspend fun testSuspend(): String {
		delay(1000)
		return "1111"
	}
}