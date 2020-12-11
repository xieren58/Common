package com.example.common.viewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.common.fragment.DemoDataBindFragment
import com.rain.baselib.viewModel.BaseViewModel

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
    }
}