package com.example.common.viewModel

import android.content.Intent
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
 *  Date: 2021/6/28
 */
class RecyclerListViewModel : BaseRecViewModel<PatientMainModel>() {
    override val adapter by lazy { PatientLoadStateAdapter() }
    override val isOpenFirstLoad = false
    var normalPosition: Int = 0
    private var normalList: MutableList<PatientMainModel>? = null
    fun initList(intent: Intent) {
        this.normalPosition = intent.getIntExtra("position", 0)
        normalList = intent.getParcelableArrayListExtra("loadList")
    }
    
    override fun initModel() {
        super.initModel()
        loadSuccess(normalList)
    }
    
    override fun loadData() {
        launchFlow {
            RetrofitFac.iData.loadPatientList(pageIndex = pageIndex, pageSize = 10)
        }.resultFail {
            loadFail()
        }.resultSuccessScope(viewModelScope) {
            loadSuccess(it)
        }
    }
}