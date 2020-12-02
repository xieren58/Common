package com.example.common

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.common.databinding.ActivityMainBinding
import com.example.common.viewModel.MainViewModel
import com.rain.baselib.activity.BaseActivity
import com.rain.baselib.common.singleClick
import com.rain.baselib.common.startAc
import com.says.common.file.PushFileManager

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    override val variableId = BR.mainId
    override fun onResume() {
        super.onResume()
        val pushMap = PushFileManager.pushMap
        Log.d("pushTag", "pushMap:${pushMap.size}")
    }

    override fun initEvent() {
        super.initEvent()
        viewBind.tvStart.singleClick {
            startAc<DemoActivity>()
        }
    }
}