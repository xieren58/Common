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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException

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
	
	fun testBreak(str: String?) {
		Log.d("checkBreakTag", "check-start:$str")
		viewModelScope.launch {
			launchFlow {
				Log.d("flowCheckTag", "flow-currentThread-launchFlow:${Thread.currentThread().name}")
				RetrofitFac.iData.getColumnConfig("9bfc78a8-6d2e-47b9-94cb-c3f40dfd9dc7",
						"ac501764-48b4-4c71-b52b-23b9bfc4e5f5",
						"857aa151-0574-4153-a98c-8084daac3296",
				"f7b6387f-42e3-4edd-b9e3-6753b75b500c",
						"",
						"fc5f739e-5be3-4346-bd27-9b774b119429",
						"2","1")
			}.transformIOFlow {
				
				Log.d("flowCheckTag", "flow-currentThread-transformIOFlow:${Thread.currentThread().name}")
				Log.d("flowCheckTag", "flow-content:${it?.content}")
				it?.content
			}.resultFail {
				Log.d("flowCheckTag", "flow-currentThread-resultFail:${Thread.currentThread().name}")
				Log.d("flowCheckTag", "flow-resultFail:${it.message}")
			}.resultSuccess {
				Log.d("flowCheckTag", "flow-currentThread-resultSuccess:${Thread.currentThread().name}")
				Log.d("flowCheckTag", "flow-resultSuccess:${it?.Fields}")
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