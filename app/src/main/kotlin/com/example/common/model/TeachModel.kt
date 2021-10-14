package com.example.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 *  Create by rain
 *  Date: 2020/3/19
 */
@Parcelize
data class TeachModel(
        var ab: String? = null,
        var classes: String? = null,
        var docId: String? = null,
        var titleCh: String? = null,
        var updateTime: String? = null,
        var img: String? = null,
        var type: Int = -1,
) : Parcelable