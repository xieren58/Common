package com.example.common.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.viewModel.PatientMainFgViewModel
import com.example.common.R
import com.example.common.databinding.FgMainPatientBinding
import com.rain.baselib.fragment.BaseRecFragment

/**
 * 受试者列表页
 */
class PatientFragment : BaseRecFragment<FgMainPatientBinding>() {
	override val layoutResId = R.layout.fg_main_patient
	override val viewModel by viewModels<PatientMainFgViewModel>()
	
	
	override fun getRecLayoutManager() = LinearLayoutManager(context)
	
	override fun getEmptyText() = "暂无进行中记录"
	override fun onStart() {
		super.onStart()
		callRefreshView()
	}
	
	override fun clickRecItem(position: Int) {
	}
}