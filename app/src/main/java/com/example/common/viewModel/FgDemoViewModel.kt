package com.example.common.viewModel

import androidx.lifecycle.viewModelScope
import com.example.common.adapter.PatientLoadStateAdapter
import com.example.common.http.RetrofitFac
import com.example.common.http.scope.launchFlow
import com.example.common.http.scope.resultFail
import com.example.common.http.scope.resultScope
import com.example.common.http.scope.resultSuccessScope
import com.example.common.model.PatientMainModel
import com.rain.baselib.viewModel.BaseRecViewModel

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class FgDemoViewModel : BaseRecViewModel<PatientMainModel>() {
	override val adapter by lazy { PatientLoadStateAdapter() }
	
	override fun loadData() {
		launchFlow {
			RetrofitFac.iData.loadPatientList(pageIndex = pageIndex,pageSize = 10,)
		}.resultFail {
			loadFail()
		}.resultSuccessScope(viewModelScope){
			loadSuccess(it)
		}
	}
}