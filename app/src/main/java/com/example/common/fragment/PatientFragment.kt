package com.example.common.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.common.viewModel.PatientMainFgViewModel
import com.example.common.R
import com.example.common.databinding.FgMainPatientBinding
import com.google.android.exoplayer2.transformer.Transformer
import com.rain.baselib.fragment.BaseDataBindFragment

/**
 * 受试者列表页
 */
class PatientFragment : BaseDataBindFragment<FgMainPatientBinding>() {
	override val layoutResId = R.layout.fg_main_patient
	override val viewModel by viewModels<PatientMainFgViewModel>()
	override fun initView() {
		super.initView()
		viewBind.viewPagerScroll.adapter = viewModel.adapter
//		viewBind.viewPagerScroll.orientation = ViewPager2.ORIENTATION_VERTICAL
		val compositePageTransformer = CompositePageTransformer().apply {
//			addTransformer(MarginPageTransformer(100))
			addTransformer(DemoTransformer())
		}
		viewBind.flContainer.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
//		compositePageTransformer.addTransformer(ScaleInTransformer())
		viewBind.viewPagerScroll.setPageTransformer (compositePageTransformer)
		viewBind.viewPagerScroll.offscreenPageLimit = 3
	}
	override fun onStart() {
		super.onStart()
	}
}