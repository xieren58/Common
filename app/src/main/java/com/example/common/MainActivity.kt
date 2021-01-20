package com.example.common

import android.util.Log
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
	private var accuracy =3
	override fun initEvent() {
		super.initEvent()
		viewBind.tvStart.singleClick {
//			startAc<DemoActivity>()
			viewModel.updateLevel()
		}
		viewBind.tvEnd.singleClick {
			startAc<Demo2DataBindActivity>()
		}
		viewBind.content.singleClick {
			viewBind.etNumber.clearFocus()
		}
		viewBind.etNumber.setOnFocusChangeListener { _, hasFocus ->
			if (hasFocus) return@setOnFocusChangeListener
			val textContent = viewBind.etNumber.text.toString().trim()
			val keepNumberStr = NumberUtils.getKeepNumberStr(this, textContent, accuracy)
			Log.d("editTag", "keepNumberStr:$keepNumberStr")
			if (!keepNumberStr.isNullOrEmpty()) {
				viewBind.etNumber.setText(keepNumberStr)
			}
		}
	}
}