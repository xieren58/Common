package com.example.common.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.common.BR
import com.example.common.R
import com.example.common.databinding.FgDemoBinding
import com.example.common.viewModel.FgDemoViewModel
import com.rain.baselib.fragment.BaseDataBindFragment

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class DemoDataBindFragment : BaseDataBindFragment<FgDemoBinding>() {
	override val layoutResId = R.layout.fg_demo
	override val viewModel by lazy { ViewModelProvider(this).get(FgDemoViewModel::class.java) }
	companion object {
		fun getInstance(index: Int): DemoDataBindFragment {
			return DemoDataBindFragment().apply {
				arguments = Bundle().apply {
					this.putInt("index", index)
				}
			}
		}
	}
	
	override val variableId = BR.fgDemoId
	override fun initModelObserve() {
		super.initModelObserve()
		viewModel.setIndexData(arguments?.getInt("index") ?: 0)
	}
}