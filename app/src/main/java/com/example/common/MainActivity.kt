package com.example.common

import androidx.activity.viewModels
import com.example.common.databinding.ActivityMainBinding
import com.example.common.viewModel.MainViewModel
import com.rain.baselib.activity.BaseDataBindActivity
import com.rain.baselib.common.singleClick
import com.rain.baselib.common.startAc

class MainActivity : BaseDataBindActivity<ActivityMainBinding>() {
	override val layoutResId = R.layout.activity_main
	override val viewModel by viewModels<MainViewModel>()
	override val variableId = BR.mainId
	override fun initEvent() {
		super.initEvent()
		viewBind.tvStart.singleClick {
			startAc<DemoActivity>()
		}
		viewBind.tvEnd.singleClick {
			startAc<Demo2DataBindActivity>()
		}
	}
}