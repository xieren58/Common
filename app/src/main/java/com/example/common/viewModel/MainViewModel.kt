package com.example.common.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.common.Logger
import com.example.common.http.Api
import com.example.common.http.RetrofitFac
import com.example.common.model.DemoModel
import com.rain.baselib.viewModel.BaseViewModel
import com.says.common.ui.isNull
import com.says.common.utils.DataStoreCommon
import com.says.common.utils.JsonManagerHelper
import kotlinx.coroutines.launch

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class MainViewModel : BaseViewModel() {

    fun updateLevel() {
        Log.d("ApiPropertiesTag", "BASE_URL:${Api.BASE_URL}")
        viewModelScope.launch {
            val str:String?=null
            val i = str.isNull {
                Log.d("ApiPropertiesTag", "isNull")
            }
            Log.d("ApiPropertiesTag", "i:$i")
//            DataStoreCommon.putValue()
//            DataStoreCommon.putValue("test" to 10000, "test2" to "hha")
        }

    }

    fun queryLevel() {
        viewModelScope.launch {
            val otherValue = DataStoreCommon.getOtherValue("test", 0)
            val test2 = DataStoreCommon.getOtherValue("test2", "")
            Log.d("dataStoreTag", "otherValue:$otherValue,test2:$test2")
        }
    }

    private val patientGroupId = 49
    private fun getMutableLevel(
        patientList: MutableList<DemoModel.DemoDetailModel>,
        patientId: Int,
        index: Int = 1
    ): Int {
        patientList.forEach {
            var indexPosition = index
            if (patientId != patientGroupId && it.Id == patientId) {
                indexPosition++
                val mutablePatient = getMutableLevel(patientList, it.ParentId, indexPosition)
                if (mutablePatient == 1) return indexPosition
            }
        }
        return 1
    }
}