package com.example.common.activity

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.common.R
import com.example.common.databinding.ActivityStartNavigationBinding
import com.rain.baselib.activity.BaseDataBindActivity
import com.rain.baselib.common.navigationPopUpTo
import com.rain.baselib.viewModel.BaseViewModel

/**
 *  Create by rain
 *  Date: 2021/3/9
 */
class StartNavigationActivity : BaseDataBindActivity<ActivityStartNavigationBinding>() {
	override val layoutResId = R.layout.activity_start_navigation
	override val viewModel: BaseViewModel? = null
	private var isLoginOut: Boolean = false
	override fun initIntent(savedInstanceState: Bundle?) {
		super.initIntent(savedInstanceState)
		isLoginOut = intent.getBooleanExtra("loginOut", false)
	}
	
	override fun initData() {
		super.initData()
		if (isLoginOut)navigationPopUpTo(R.id.nav_start_home_fragment,R.id.fg_login_id)
	}
	
	override fun onSupportNavigateUp(): Boolean {
		return Navigation.findNavController(this, R.id.nav_start_home_fragment).navigateUp()
	}
}