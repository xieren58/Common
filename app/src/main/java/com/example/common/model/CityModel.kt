package com.example.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *  Create by rain
 *  Date: 2020/12/14
 */
@Parcelize
data class CityModel(
    val name: String? = null,//":"Gatundu South",
    val value: String? = null, //":"1003002",
    val level: Int = 0, //":2,
    val parent: String? = null, // ":"1003"
    val isOpen: Boolean = false,
    val childCityList: MutableList<CityModel>? = null
) : Parcelable