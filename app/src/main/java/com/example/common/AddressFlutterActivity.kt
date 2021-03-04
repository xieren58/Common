package com.example.common

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.MethodChannel


/**
 *  Create by rain
 *  Date: 2021/3/4
 */
class AddressFlutterActivity : FlutterActivity() {
	//	private val engine by lazy {
//		FlutterEngineCache.getInstance().get("flutter_address_id") ?: FlutterEngine(this)
//	}
	private val methodChannel by lazy { MethodChannel(flutterEngine?.dartExecutor?.binaryMessenger, "flutter_address_open") }
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		methodChannel.setMethodCallHandler { call, result ->
			Log.d("flutterTag", "method:${call.method}")
			Log.d("flutterTag", "arguments:${call.arguments}")
			Log.d("flutterTag", "result:${result}")
		}
		setNormalAddress()
	}
	
	override fun getFlutterEngine(): FlutterEngine? {
		val engineCache = FlutterEngineCache.getInstance().get("flutter_address_id")
		Log.d("flutterTag", "engineCache:$engineCache")
		return engineCache
	}
	
	private fun setNormalAddress() {
		methodChannel.invokeMethod("cityId", "1")
		methodChannel.invokeMethod("provenId", "2")
		methodChannel.invokeMethod("areaId", "3")
	}

//	override fun onResume() {
//		super.onResume()
//		engine.lifecycleChannel.appIsResumed()
//	}
//
//
//	override fun onStop() {
//		super.onStop()
//		engine.lifecycleChannel.appIsPaused();
//	}
//
//	override fun onPause() {
//		super.onPause()
//		engine.lifecycleChannel.appIsInactive()
//	}
	
}