package com.example.common.http.scope

import android.util.Log
import com.example.common.model.BaseResponse
import com.rain.baselib.common.NetWorkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 *  Create by rain
 *  Date: 2020/6/29
 *  网络请求 协程扩展类
 */
fun <T> CoroutineScope.launchNoUI(block: suspend () -> BaseResponse<T>, successBlock: (T?) -> Unit, failBlock: (message: String?) -> Unit) {
	Log.d("testLaunchTag", "launchUI-noLine-:$this")
	launchMessageNoUI(block, { _, data ->
		Log.d("testLaunchTag", "launchUI-noLine--successBlock:$data")
		successBlock(data)
	}, {
		Log.d("testLaunchTag", "launchUI-noLine--failBlock:$it")
		failBlock(it)
	})
}

fun <T> CoroutineScope.launchMessageNoUI(block: suspend () -> BaseResponse<T>, successBlock: (String?, T?) -> Unit, failBlock: (message: String?) -> Unit) {
	Log.d("testLaunchTag", "launchMessageUI-noLine-:$this")
	if (!NetWorkUtils.isNetConnected()) {
		failBlock("请检查网络连接")
		Log.d("testLaunchTag", "launchMessageUI-noLine--failBlock:$this")
		return
	}
	launch(Dispatchers.Main) {
		Log.d("testLaunchTag", "launchMessageUI-noLine--launch:$this")
		kotlin.runCatching {
			block()
		}.onSuccess {
			if (it.code != 1) failBlock(it.msg) else successBlock(it.msg, it.data)
		}.onFailure {
			if (it is HttpException) failBlock(it.message()) else failBlock(it.message)
		}
	}
}