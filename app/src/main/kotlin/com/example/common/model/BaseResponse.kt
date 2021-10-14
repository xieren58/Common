package com.example.common.model

import com.google.gson.annotations.SerializedName

/**
 *  Create by rain
 *  Date: 2020/12/14
 */
data class BaseResponse<T>(
        @SerializedName("Result")
        var result: Int = 0,
        @SerializedName("ErrorMsg")
        var errorMsg: String = "",
        @SerializedName("Data")
        var data: T? = null,
        @SerializedName("TotalCount")
        var totalCount: Int = 0,
)