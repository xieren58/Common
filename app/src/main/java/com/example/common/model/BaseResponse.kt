package com.example.common.model

/**
 *  Create by rain
 *  Date: 2020/12/14
 */
class BaseResponse<T> {
    var status: Int = 0
    var code: String? = null
    var message: String? = null
    var data: T? = null
}