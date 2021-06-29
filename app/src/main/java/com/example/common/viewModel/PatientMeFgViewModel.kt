package com.example.common.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rain.baselib.viewModel.BaseViewModel
import com.says.common.utils.Common
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  Create by rain
 *  Date: 2021/5/11
 */
class PatientMeFgViewModel : BaseViewModel() {

    val sex = MutableLiveData<Int>() //头像
    val nickname = MutableLiveData<String>() //名称
    val testAddress = MutableLiveData<String>()//测试地点
    val testName = MutableLiveData<String>()//测试人员

    val cacheSize = MutableLiveData<String>()//缓存
    override fun initModel() {
        super.initModel()
        sex.value =   1
        nickname.value =  "测试"
        testName.value =  "测试名称"
        testAddress.value = "测试地址"
    }


    fun initCache() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cacheSize.postValue(Common.getTotalCacheSize())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 退出登录
     */
    fun loginOut() {
    }
}