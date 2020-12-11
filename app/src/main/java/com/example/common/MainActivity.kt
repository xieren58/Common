package com.example.common

import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.common.databinding.ActivityMainBinding
import com.example.common.viewModel.MainViewModel
import com.rain.baselib.activity.BaseDataBindActivity
import com.rain.baselib.common.singleClick
import com.rain.baselib.common.startAc
import com.says.common.file.PushFileManager

class MainActivity : BaseDataBindActivity<ActivityMainBinding>() {
    override val layoutResId = R.layout.activity_main
    override val viewModel by viewModels<MainViewModel>()
    override val variableId = BR.mainId
    override fun onResume() {
        super.onResume()
        val pushMap = PushFileManager.pushMap
        Log.d("pushTag", "pushMap:${pushMap.size}")
    }

    override fun initEvent() {
        super.initEvent()
        viewBind.tvStart.singleClick {
            startAc<Demo2DataBindActivity>()
        }
    }
}