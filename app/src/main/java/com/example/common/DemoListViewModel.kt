package com.example.common

import com.rain.baselib.viewModel.BaseRecViewModel

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class DemoListViewModel:BaseRecViewModel<UpdatePic>() {
    override val adapter by lazy { PhotoWeightAdapter() }

    fun addPhoto(list:MutableList<String>){
        val dataList:MutableList<UpdatePic> = mutableListOf()
        list.forEach {
            dataList.add(UpdatePic(it))
        }
        adapter.addItemData(dataList)
    }
    override fun loadData() {
        adapter.setData(null)
        loadEnd.value = true
        showDataType()
    }
}