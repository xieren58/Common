package com.example.common.http.scope

import android.util.Log
import com.rain.baselib.common.NetWorkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 *  Create by rain
 *  Date: 2020/6/29
 *  网络请求 协程扩展类
 */

fun <T> launchFlow(block: suspend () -> BaseResponseBody<T>) = flow {
    if (!NetWorkUtils.isNetConnected()) throw ResultThrowable(-1, "请检查网络连接")
    emit(block().resultDataBlock())
}.flowOn(Dispatchers.IO).completionResult()


/**
 * 异步转换数据
 */
fun <T, R> Flow<T>.transformIOFlow(block: suspend(T) -> R) = transform {
    emit(block(it))
}.flowOn(Dispatchers.IO)

/**
 * 多次请求
 */
fun <T, R> Flow<T>.transformFlow(block: suspend (T) -> BaseResponseBody<R>) = transform {
    if (!NetWorkUtils.isNetConnected()) throw ResultThrowable(-1, "请检查网络连接")
    emit(block(it).resultDataBlock())
}.flowOn(Dispatchers.IO).completionResult()

fun <T> BaseResponseBody<T>.resultDataBlock(): T? {
    val code = code
    if (code != HttpCode.CODE_SUCCESS) {
        when (code) {
            HttpCode.TOKEN_PARAM_ERROR -> throw ResultThrowable(HttpCode.CODE_RESULT_INVALID, "")
            HttpCode.CODE_CONNECTION_FAILED, HttpCode.SERVER_ERROR -> throw ResultThrowable(HttpCode.CODE_RESULT_INVALID, "服务器异常")
            else -> throw ResultThrowable(code, msg)
        }
    }
    return data
}

/**
 * 数据执行失败
 */
fun <T> Flow<T>.resultFail(failBlock: (e: ResultThrowable) -> Unit) = catch { cause ->
    Log.e("launchFlowTag", "cause:$cause")
    when (cause) {
        is HttpException -> failBlock(ResultThrowable(cause.code(), cause.message))
        is ResultThrowable -> failBlock(cause)
        else -> failBlock(ResultThrowable(cause.message))
    }
}

/**
 * 数据执行失败
 */
fun <T> Flow<T>.resultMessageFail(failBlock: (message: String?) -> Unit) = catch { cause ->
    Log.e("launchFlowTag", "cause:$cause")
    failBlock(cause.message)
}

/**
 * 数据执行成功
 */
suspend fun <T> Flow<T>.resultSuccess(successBlock: (T?) -> Unit) = collectLatest {
    successBlock(it)
}

/**
 * 数据执行成功
 */
fun <T> Flow<T?>.resultSuccessScope(scope: CoroutineScope, successBlock: ((T?) -> Unit)?) = scope.launch {
    this@resultSuccessScope.collectLatest { successBlock?.invoke(it) }
}

/**
 * 数据执行开始，并且获取回调
 */
fun <T> Flow<T?>.resultScope(scope: CoroutineScope, successBlock: ((T?) -> Unit)?, failBlock: ((e: ResultThrowable) -> Unit)?) = scope.launch {
    this@resultScope.catch { cause ->
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
    }.collectLatest {
        successBlock?.invoke(it)
    }
}

/**
 *  获取数据结果
 */
suspend fun <T> Flow<T>.resultCall(successBlock: (T?) -> Unit, failBlock: (e: ResultThrowable) -> Unit) = catch { cause ->
    when (cause) {
        is HttpException -> failBlock(ResultThrowable(cause.code(), cause.message))
        is ResultThrowable -> failBlock(cause)
        else -> failBlock(ResultThrowable(cause.message))
    }
}.collectLatest {
    successBlock(it)
}

/**
 * 同步接口访问，返回成功值，错误将抛出异常
 * isCatch: 是否捕获异常，如果捕获，错误时返回null
 */
suspend fun <T> resultHttpData(isCatch: Boolean = true, block: suspend () -> BaseResponseBody<T>): T? {
    if (!NetWorkUtils.isNetConnected()) if (isCatch) return null else throw ResultThrowable(-1, "请检查网络连接")
    return if (isCatch) {
        try {
            block().resultDataBlock()
        } catch (e: Exception) {
            null
        }
    } else block().resultDataBlock()
}


/**
 *  错误拦截，公共处理模块
 */
private fun <T> Flow<T>.completionResult() = onCompletion { cause ->
    Log.e("resultDataTag", "cause:$cause")
}


/**
 *  异常类
 */
class ResultThrowable(val code: Int = -1, resultMessage: String?) : Throwable(resultMessage) {
    constructor(resultMessage: String?) : this(-1, resultMessage)
}