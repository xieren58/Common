package com.example.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rain.baselib.common.startAc
import com.says.common.file.PushFileManager
import com.says.common.ui.singleClick
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		tv_start.singleClick {
			startAc<DemoActivity>()
		}
	}
	
	override fun onResume() {
		super.onResume()
		val pushMap = PushFileManager.pushMap
		Log.d("pushTag","pushMap:${pushMap.size}")
	}
}