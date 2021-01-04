package com.example.common.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.common.Logger
import com.example.common.http.RetrofitFac
import com.example.common.model.DemoModel
import com.rain.baselib.viewModel.BaseViewModel
import com.says.common.utils.JsonManagerHelper
import kotlinx.coroutines.launch

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class MainViewModel : BaseViewModel() {
	
	fun updateLevel() {
		showLoadDialog()
		viewModelScope.launch {
			runCatching {
				RetrofitFac.iData.loadDemo("v1.1.38", 1.0, false, "android", 5, 1, "2020-12-21")
			}.onSuccess {
				dismissDialog()
				val mutableList = it.Data ?: return@launch
				mutableList.forEach {tableIt->
					tableIt.Level =	if (tableIt.ParentId == patientGroupId){
						1
					}else{
						val mutableLevel = getMutableLevel(mutableList, tableIt.ParentId)
						if (mutableLevel == 1) -1 else mutableLevel
					}
					Log.d("levelTag", "mutableLevel:${tableIt.Level }")
				}
				Logger.d("levelTag","mutableList:${JsonManagerHelper.getHelper().objToStr(mutableList)}")
			}.onFailure {
				dismissDialog()
			}
			
		}
		
		
	}
	
	private val patientGroupId = 49
	private fun getMutableLevel(patientList: MutableList<DemoModel.DemoDetailModel>, patientId: Int,index:Int = 1): Int {
		patientList.forEach {
			var indexPosition = index
			if (patientId != patientGroupId && it.Id == patientId) {
				indexPosition++
				val mutablePatient = getMutableLevel(patientList, it.ParentId,indexPosition)
				if (mutablePatient == 1) return indexPosition
			}
		}
		return 1
	}
}