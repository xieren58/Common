package com.example.common.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rain.baselib.viewModel.BaseViewModel

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class FgDemoViewModel :BaseViewModel() {
    val indexLiveData =MutableLiveData<String>()

    fun setIndexData(index:Int){
        Log.d("indexTag","index:$index")
        indexLiveData.value = index.toString()
    }
}