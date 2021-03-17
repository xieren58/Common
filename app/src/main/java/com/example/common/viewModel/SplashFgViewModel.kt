package com.example.common.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rain.baselib.viewModel.BaseViewModel
import com.says.common.utils.DataStoreCommon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *  Create by rain
 *  Date: 2021/3/9
 */
class SplashFgViewModel : BaseViewModel() {
    override fun initModel() {
        initMainType()
    }

    val isMainType = MutableLiveData<Boolean>()
    private fun initMainType() {
        viewModelScope.launch {
            delay(1000)
            val otherValue = DataStoreCommon.getOtherValue("isLogin", false)
            isMainType.value = otherValue
        }
    }
}