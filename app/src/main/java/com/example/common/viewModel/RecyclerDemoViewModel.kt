package com.example.common.viewModel

import androidx.lifecycle.MutableLiveData
import com.rain.baselib.viewModel.BaseViewModel

/**
 *  Create by rain
 *  Date: 2021/6/28
 */
class RecyclerDemoViewModel :BaseViewModel() {
	val name = MutableLiveData<String>()
}