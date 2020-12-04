package com.example.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.common.BR
import com.example.common.databinding.FgDemoBinding
import com.example.common.viewModel.FgDemoViewModel
import com.rain.baselib.fragment.BaseFragment

/**
 *  Create by rain
 *  Date: 2020/12/1
 */
class DemoFragment : BaseFragment<FgDemoBinding, FgDemoViewModel>() {
	companion object {
		fun getInstance(index: Int): DemoFragment {
			return DemoFragment().apply {
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