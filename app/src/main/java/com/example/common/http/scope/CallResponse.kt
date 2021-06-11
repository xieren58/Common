package com.example.common.http.scope

import android.util.Log
import com.example.common.model.BaseResponse
import com.rain.baselib.common.NetWorkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

/**
 *  Create by rain
 *  Date: 2020/6/29
 *  网络请求 协程扩展类
 */
fun <T> launchFlow(block:suspend () -> BaseResponse<T>) = flow {
    if (!NetWorkUtils.isNetConnected()) throw ResultThrowable(-1, "请检查网络连接")
    val resultBlock = block()
    if (resultBlock.result != 1) throw ResultThrowable(resultBlock.result, resultBlock.errorMsg)
    emit(resultBlock.data)
}.flowOn(Dispatchers.IO).completionResult()

/**
 * 异步转换数据
 */
fun <T, R> Flow<T>.transformIOFlow(block: (T) -> R) = transform {
    emit(block(it))
}.flowOn(Dispatchers.IO)

/**
 * 二次请求
 */
fun <T, R> Flow<T>.transformFlow(block: suspend (T) -> BaseResponse<R>) = transform {
    if (!NetWorkUtils.isNetConnected()) throw ResultThrowable(-1, "请检查网络连接")
    val resultBlock = block(it)
    if (resultBlock.result != 1) throw ResultThrowable(resultBlock.result, resultBlock.errorMsg)
    emit(resultBlock.data)
}.flowOn(Dispatchers.IO).completionResult()

private fun <T> Flow<T>.completionResult() = onCompletion { cause ->
//	if (cause == null) return@onCompletion
//	if (cause !is ResultThrowable) return@onCompletion
//	when (cause.code) {
//		10 -> {
//			Log.d("")
//		}
//		11 -> TipsShowUtils.showTipsDialog(cause.message)
//	}
}

suspend fun <T> Flow<T>.resultCall(
    successBlock: (T?) -> Unit,
    failBlock: (e: ResultThrowable) -> Unit
) = catch { cause ->
    when (cause) {
        is HttpException -> failBlock(ResultThrowable(cause.code(), cause.message))
        is ResultThrowable -> {
            when (cause.code) {
                10, 11 -> failBlock(ResultThrowable(cause.code, ""))
                else -> failBlock(cause)
            }
        }
        else -> failBlock(ResultThrowable(cause.message))
    }
}.collectLatest {
    successBlock(it)
}

/**
 * 数据执行失败
 */

fun <T> Flow<T>.resultFail(failBlock: (e: ResultThrowable) -> Unit) = catch { cause ->
    Log.d("launchFlowTag", "cause:$cause")
    when (cause) {
        is HttpException -> failBlock(ResultThrowable(cause.code(), cause.message))
        is ResultThrowable -> {
            when (cause.code) {
                10, 11 -> failBlock(ResultThrowable(cause.code, ""))
                else -> failBlock(cause)
            }
        }
        else -> failBlock(ResultThrowable(cause.message))
    }
}

/**
 * 数据执行失败
 */
fun <T> Flow<T>.resultMessageFail(failBlock: (message: String?) -> Unit) = catch { cause ->
    Log.d("launchFlowTag", "cause:$cause")
    when (cause) {
        is ResultThrowable -> {
            when (cause.code) {
                10, 11 -> failBlock("")
                else -> failBlock(cause.message)
            }
        }
        else -> failBlock(cause.message)
    }
}

/**
 * 数据执行成功
 */
suspend fun <T> Flow<T>.resultSuccess(successBlock: (T?) -> Unit) = collectLatest {
    successBlock(it)
}
fun <T> Flow<T?>.resultScope(scope:CoroutineScope,successBlock: ((T?) -> Unit)?, failBlock: ((e: ResultThrowable) -> Unit)?) = scope.launch {
    this@resultScope.catch {cause->
        when (cause) {
            is HttpException -> failBlock?.invoke(ResultThrowable(cause.code(), cause.message))
            is ResultThrowable -> {
                when (cause.code) {
                    11 -> failBlock?.invoke(ResultThrowable(cause.code, ""))
                    else -> failBlock?.invoke(cause)
                }
            }
            else -> failBlock?.invoke(ResultThrowable(cause.message))
        }
    }.collectLatest{
        successBlock?.invoke(it)
    }
}
fun <T> Flow<T?>.resultScope(scope:CoroutineScope,successBlock: ((T?) -> Unit)?) = scope.launch {
    this@resultScope.collectLatest{ successBlock?.invoke(it) }
}

fun <T> CoroutineScope.launchCountUI(
    block: suspend () -> BaseResponse<T>,
    successBlock: (T?, Int) -> Unit,
    failBlock: (e: ResultThrowable) -> Unit
) = launchDataUI(block, { _, t, c ->
    successBlock(t, c)
}, failBlock)

fun <T> CoroutineScope.launchUI(
    block: suspend () -> BaseResponse<T>,
    successBlock: (T?) -> Unit,
    failBlock: (e: ResultThrowable) -> Unit
) = launchDataUI(block, { _, t, _ ->
    successBlock(t)
}, failBlock)

fun <T> CoroutineScope.launchMessageUI(
    block: suspend () -> BaseResponse<T>,
    successBlock: (String?, T?) -> Unit,
    failBlock: (e: ResultThrowable) -> Unit
) = launchDataUI(block, { s, t, _ ->
    successBlock(s, t)
}, failBlock)

fun <T> CoroutineScope.launchDataUI(
    block: suspend () -> BaseResponse<T>,
    successBlock: (String?, T?, Int) -> Unit,
    failBlock: (e: ResultThrowable) -> Unit
) = launch(Dispatchers.Main) {
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
        if (it is HttpException) failBlock(ResultThrowable(it.code(), it.message)) else failBlock(
            ResultThrowable(it.message)
        )
    }
}

class ResultThrowable(val code: Int = -1, resultMessage: String?) : Throwable(resultMessage) {
    constructor(resultMessage: String?) : this(-1, resultMessage)
}

fun <T> BaseResponse<T>.resultData(
    block: (String?, T?, Int) -> Unit,
    failBlock: (e: ResultThrowable) -> Unit
) {
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