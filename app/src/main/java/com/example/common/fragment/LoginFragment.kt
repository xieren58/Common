package com.example.common.fragment

import com.example.common.R
import com.example.common.databinding.FgLoginHomeBinding
import com.rain.baselib.common.navigationPopUpTo
import com.rain.baselib.common.singleClick
import com.rain.baselib.fragment.BaseDataBindFragment
import com.rain.baselib.viewModel.BaseViewModel

/**
 *  Create by rain
 *  Date: 2021/3/9
 */
class LoginFragment : BaseDataBindFragment<FgLoginHomeBinding>() {
	override val layoutResId = R.layout.fg_login_home
	override val viewModel: BaseViewModel? = null
	override fun initEvent() {
		super.initEvent()
		viewBind.tvLogin.singleClick {
			navigationPopUpTo(R.id.login_start_main)
			activity?.finish()
		}
	}
}