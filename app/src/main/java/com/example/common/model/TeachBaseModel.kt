package com.example.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *  Create by rain
 *  Date: 2020/3/19
 */
@Parcelize
data class TeachBaseModel(
    val pageIndex: Int = 0,
    var sufferingList: MutableList<TeachModel>? = null
) : Parcelable