package com.example.common.http.scope

/**
 *  Create by rain
 *  Date: 2021/8/30
 */
data class BaseResponseBody<T>(
		var code: Int = 0,
		
		var msg: String? = null,
		
		var data: T? = null,
		
		var language: String? = null,
		
		var st: Long = 0,
)

object HttpCode {
	const val CODE_EMPTY_DATA = -3 //请求body空数据
	const val CODE_THROWABLE = -2 //请求Error Throwable
	const val CODE_UNKNOWN = -1
	const val CODE_SUCCESS = 0
	const val CODE_TOKEN_EXPIRED = 9
	const val CODE_ACCOUNT_INVALID = 3
	const val CODE_RESULT_INVALID = 4
	const val ResultInvalidException = 5
	const val CODE_CONNECTION_FAILED = 6
	const val SERVER_ERROR = 1
	const val TOKEN_PARAM_ERROR = 8
	const val TOKEN_INVALID = 10
}