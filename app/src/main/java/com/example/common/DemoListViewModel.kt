package com.example.common

import com.example.common.PhotoWeightAdapter.Companion.ADD_TYPE
import com.rain.baselib.viewModel.BaseRecViewModel

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class DemoListViewModel : BaseRecViewModel<UpdatePic>() {
    override val adapter by lazy { PhotoWeightAdapter() }

    fun addPhoto(list: MutableList<String>) {
        val dataList: MutableList<UpdatePic> = mutableListOf()
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

    fun getPhotoList(): MutableList<UpdatePic> {
        val photoLists = mutableListOf<UpdatePic>()
        getLists().forEach {
            if (it.itemType != ADD_TYPE) photoLists.add(it)
        }
        return photoLists
    }
}