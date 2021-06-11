package com.example.common

import android.util.Log
import com.example.common.adapter.PhotoWeightAdapter
import com.rain.baselib.viewModel.BaseRecViewModel

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class DemoListViewModel : BaseRecViewModel<UpdatePic>() {
    override val adapter by lazy { PhotoWeightAdapter() }
    companion object{
        fun test(){
            Log.d("catchMethodTag","test")
        }
    }

    override fun loadData() {
        adapter.addItemData(UpdatePic().apply {
            itemType = PhotoWeightAdapter.ADD_TYPE
        })
        showDataType()
    }

}