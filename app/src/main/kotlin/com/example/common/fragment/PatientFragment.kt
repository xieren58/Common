package com.example.common.fragment

import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.common.viewModel.PatientMainFgViewModel
import com.example.common.R
import com.example.common.databinding.FgMainPatientBinding
import com.google.android.exoplayer2.transformer.Transformer
import com.rain.baselib.fragment.BaseDataBindFragment
import com.says.common.ui.UICommon

/**
 * 受试者列表页
 */
class PatientFragment : BaseDataBindFragment<FgMainPatientBinding>() {
    override val layoutResId = R.layout.fg_main_patient
    override val viewModel by viewModels<PatientMainFgViewModel>()
    override fun initView() {
        super.initView()
        viewBind.viewPagerScroll.adapter = viewModel.adapter
        val compositePageTransformer = CompositePageTransformer().apply {
			addTransformer(MarginPageTransformer(UICommon.dip2px(requireContext(),20F)))
//            addTransformer(DemoTransformer())
        }
        viewBind.flContainer.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//		compositePageTransformer.addTransformer(ScaleInTransformer())
        viewBind.viewPagerScroll.setPageTransformer(compositePageTransformer)
        viewBind.viewPagerScroll.offscreenPageLimit = 2
    }
    
    override fun onStart() {
        super.onStart()
    }
}