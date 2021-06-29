package com.example.common.fragment

import androidx.fragment.app.viewModels
import com.example.common.BR
import com.example.common.viewModel.PatientMeFgViewModel
import com.example.common.R
import com.example.common.databinding.FgMainMeBinding
import com.rain.baselib.fragment.BaseDataBindFragment

/**
 * 我的页面
 */
class MeFragment : BaseDataBindFragment<FgMainMeBinding>() {
	override val layoutResId = R.layout.fg_main_me
	override val viewModel by viewModels<PatientMeFgViewModel>()
	override val variableId = BR.patientMeFgViewModelId
	override fun onStart() {
		super.onStart()
		viewModel.initCache()
	}
}