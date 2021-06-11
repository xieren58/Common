package com.example.common.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rain.baselib.viewModel.BaseViewModel
import kotlinx.coroutines.launch

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class FgDemoViewModel : BaseViewModel() {
//	val adapter by lazy { PatientPagingAdapter() }
	override fun initModel() {
		super.initModel()
		loadPatientData()
	}
	
	val liveData = MutableLiveData<Boolean>()
	private fun loadPatientData() {
//		viewModelScope.launch {
//			Pager(PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 10 )) {
//				PixDataSource()
//			}.flow.cachedIn(viewModelScope).collectLatest {
//				adapter.submitData(it)
//				liveData.value = false
//			}
//		}
	}
	
	override fun onCleared() {
		super.onCleared()
		Log.d("fgDemoTag", "onCleared")
	}
}