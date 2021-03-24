package com.example.common.viewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.common.fragment.DemoDataBindFragment
import com.rain.baselib.viewModel.BaseViewModel
import com.says.common.dataStore.DataStoreCommon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class Demo2ViewModel : BaseViewModel() {
    val mTitleDataList = MutableLiveData<MutableList<String>>()
    val mPgDataList = MutableLiveData<MutableList<Fragment>>()
    override fun initModel() {
        super.initModel()
        val titleList = mutableListOf<String>()
        val titleFgList = mutableListOf<Fragment>()
        for (i in 0..10) {
            titleList.add("$i$i$i")
            titleFgList.add(DemoDataBindFragment.getInstance(i))
        }
        mTitleDataList.value = titleList
        mPgDataList.value = titleFgList


        viewModelScope.launch {
            val otherValue = DataStoreCommon.getOtherValue("test", 0)
            DataStoreCommon.putValue("test", otherValue + 1)
        }
    }
}