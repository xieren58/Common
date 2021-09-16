package com.example.common.model

import android.os.Parcelable
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.rain.baselib.BR
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 *  Create by rain
 *  Date: 2020/12/14
 */
@Parcelize
data class CityModel(
        val value: String? = null, //":"1003002",
        val level: Int = 0, //":2,
        val parent: String? = null, // ":"1003"
        val isOpen: Boolean = false,
        val childCityList: MutableList<CityModel>? = null,
) : Parcelable, BaseObservable() {
    @IgnoredOnParcel
    @Bindable
    var name: String = ""
        set(value) {
            Log.d("cityModelTag", "value:$value")
            field = value
            notifyPropertyChanged(BR.name)
        }
}