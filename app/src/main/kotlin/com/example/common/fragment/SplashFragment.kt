package com.example.common.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.common.R
import com.example.common.databinding.FgSplashHomeBinding
import com.example.common.viewModel.SplashFgViewModel
import com.rain.baselib.common.navigationPopUpTo
import com.rain.baselib.fragment.BaseDataBindFragment

/**
 *  Create by rain
 *  Date: 2021/3/9
 */
class SplashFragment : BaseDataBindFragment<FgSplashHomeBinding>() {
    override val layoutResId = R.layout.fg_splash_home
    override val viewModel by viewModels<SplashFgViewModel>()
    override fun initModelObserve() {
        super.initModelObserve()
        viewModel.isMainType.observe(this, {
            if (it) {
                navigationPopUpTo(R.id.splash_start_main)
                activity?.finish()
            } else {
                navigationPopUpTo(R.id.splash_start_login)
            }
        })
    }
}