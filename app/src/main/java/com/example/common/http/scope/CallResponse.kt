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
fun <T> CoroutineScope.launchUI(block: suspend () -> BaseResponse<T>, successBlock: (T?) -> Unit = {}, failBlock: (message: String?) -> Unit = {}) =
		launchMessageUI(block, { _, data ->
			Log.d("testLaunchTag", "launchUI-Line--successBlock:$data")
			successBlock(data)
		}, {
			Log.d("testLaunchTag", "launchUI-Line--failBlock:$it")
			failBlock(it)
		})

fun <T> CoroutineScope.launchMessageUI(block: suspend () -> BaseResponse<T>, successBlock: (String?, T?) -> Unit, failBlock: (message: String?) -> Unit = {}) = launch(Dispatchers.Main) {
	Log.d("testLaunchTag", "launchMessageUI-Line-:$this")
	if (!NetWorkUtils.isNetConnected()) {
		failBlock("请检查网络连接")
		Log.d("testLaunchTag", "launchMessageUI-Line--failBlock:$this")
		return@launch
	}
	Log.d("testLaunchTag", "launchMessageUI-Line--launch:$this")
	kotlin.runCatching {
		block()
	}.onSuccess {
		if (it.code != 1) failBlock(it.msg) else successBlock(it.msg, it.data)
	}.onFailure {
		if (it is HttpException) failBlock(it.message()) else failBlock(it.message)
	}
}

fun <T> BaseResponse<T>?.resultData(): T? {
	if (this == null) return null
	if (code != 1) {
		return null
	}
	return data
}

fun <T> BaseResponse<T>?.resultSuccess(): Boolean {
	if (this == null) return false
	return code == 1
}