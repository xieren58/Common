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
fun <T> CoroutineScope.launchUI(block: suspend () -> BaseResponse<T>, successBlock: (T?) -> Unit, failBlock: (e: ResultThrowable) -> Unit) {
	launchMessageUI(block, { _, data ->
		successBlock(data)
	}, {
		failBlock(it)
	})
}

fun <T> CoroutineScope.launchMessageUI(block: suspend () -> BaseResponse<T>, successBlock: (String?, T?) -> Unit, failBlock: (e: ResultThrowable) -> Unit) {
	if (!NetWorkUtils.isNetConnected()) {
		failBlock(ResultThrowable("请检查网络连接"))
		return
	}
	val launch = launch(Dispatchers.Main) {
		kotlin.runCatching {
			block()
		}.onSuccess {
			it.resultData({ message, data ->
				successBlock(message, data)
			}, { failIt ->
				failBlock(failIt)
			})
		}.onFailure {
			if (it is HttpException) failBlock(ResultThrowable(it.code(), "服务器连接异常，请稍候重试")) else failBlock(
				ResultThrowable("服务器连接异常，请稍候重试")
			)
		}
	}
	launch.isCompleted
}

class ResultThrowable(val code: Int = -1, resultMessage: String?) : Throwable(resultMessage) {
	constructor(resultMessage: String?) : this(-1, resultMessage)
}

fun <T> BaseResponse<T>.resultData(block: (String?, T?) -> Unit, failBlock: (e: ResultThrowable) -> Unit) {
	Log.d("launchTag", "status:${status},code:$code,data:$data")
	if (status != 1) {
		failBlock(ResultThrowable(status, message))
	} else {
		block(message, data)
	}
}

fun <T> BaseResponse<T>?.resultData(): T? {
	if (this == null) return null
	if (status != 1) {
		return null
	}
	return data
}

fun <T> BaseResponse<T>?.resultSuccess(): Boolean {
	if (this == null) return false
	return status == 1
}

fun <T, K> BaseResponse<T>.createErrResponse(): BaseResponse<K> {
	return BaseResponse<K>().apply {
		status = this@createErrResponse.status
		data = null
		message = this@createErrResponse.message
	}
}