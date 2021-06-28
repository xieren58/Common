package com.example.common.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.example.common.BR
import com.example.common.R
import com.example.common.databinding.ActivityRecyclerDemoBinding
import com.example.common.viewModel.RecyclerDemoViewModel
import com.rain.baselib.activity.BaseDataBindActivity

class RecyclerDemoActivity : BaseDataBindActivity<ActivityRecyclerDemoBinding>() {
	override val layoutResId = R.layout.activity_recycler_demo
	override val viewModel by viewModels<RecyclerDemoViewModel>()
	override val variableId  = BR.recyclerDemoViewModelId
	
	override fun initIntent(savedInstanceState: Bundle?) {
		super.initIntent(savedInstanceState)
		viewModel.name.value = intent.getStringExtra("name")?:""
	}
}