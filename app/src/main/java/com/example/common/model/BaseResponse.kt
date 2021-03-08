package com.example.common.model

/**
 *  Create by rain
 *  Date: 2020/12/14
 */
data class BaseResponse<T> (var code: Int = 0, var msg: String = "", var data: T? = null)