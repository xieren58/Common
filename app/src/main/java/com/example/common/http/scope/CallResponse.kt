package com.example.common.http.scope

import android.util.Log
import com.example.common.model.BaseResponse
import com.rain.baselib.common.NetWorkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

/**
 *  Create by rain
 *  Date: 2020/6/29
 *  网络请求 协程扩展类
 */
fun <T> CoroutineScope.launchCountUI(block: suspend () -> BaseResponse<T>, successBlock: (T?, Int) -> Unit, failBlock: (e: ResultThrowable) -> Unit) = launchDataUI(block, { _, t, c ->
	successBlock(t, c)
}, failBlock)

fun <T> CoroutineScope.launchUI(block: suspend () -> BaseResponse<T>, successBlock: (T?) -> Unit, failBlock: (e: ResultThrowable) -> Unit) = launchDataUI(block, { _, t, _ ->
	successBlock(t)
}, failBlock)

fun <T> CoroutineScope.launchMessageUI(block: suspend () -> BaseResponse<T>, successBlock: (String?, T?) -> Unit, failBlock: (e: ResultThrowable) -> Unit) = launchDataUI(block, { s, t, _ ->
	successBlock(s, t)
}, failBlock)

fun <T> CoroutineScope.launchDataUI(block: suspend () -> BaseResponse<T>, successBlock: (String?, T?, Int) -> Unit, failBlock: (e: ResultThrowable) -> Unit) = launch(Dispatchers.Main) {
	if (!NetWorkUtils.isNetConnected()) {
		failBlock(ResultThrowable("请检查网络连接"))
		return@launch
	}
	
	kotlin.runCatching {
		block()
	}.onSuccess {
		Log.d("errTag", "onSuccess-it:$it")
		it.resultData({ message, data, count ->
			successBlock(message, data, count)
		}, { failIt ->
			failBlock(failIt)
		})
	}.onFailure {
		Log.d("errTag", "onFailure-it:$it")
		if (it is HttpException) failBlock(ResultThrowable(it.code(), it.message)) else failBlock(ResultThrowable(it.message))
	}
}

class ResultThrowable(val code: Int = -1, resultMessage: String?) : Throwable(resultMessage) {
	constructor(resultMessage: String?) : this(-1, resultMessage)
}

fun <T> BaseResponse<T>.resultData(block: (String?, T?, Int) -> Unit, failBlock: (e: ResultThrowable) -> Unit) {
	if (result != 1) {
		failBlock(ResultThrowable(result, errorMsg))
		return
	}
	block(errorMsg, data, totalCount)
}

fun <T> Response<BaseResponse<T>>?.resultData(): T? {
	if (this == null) return null
	val bodyChild = body()
	Log.d("launchTag", "bodyChild:$bodyChild,isSuccessful:$isSuccessful")
	if (!isSuccessful || bodyChild == null) {
		return null
	}
	val data = bodyChild.data
	Log.d("launchTag", "code:${bodyChild.result},data:$data")
	if (bodyChild.result != 1 || data == null) {
		return null
	}
	return data
}

fun <T> BaseResponse<T>?.resultData(): T? {
	if (this == null) return null
	if (result != 1 || data == null) {
		return null
	}
	return data
}