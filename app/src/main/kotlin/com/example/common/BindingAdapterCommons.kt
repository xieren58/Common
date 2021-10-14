package com.example.common

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 *  Create by rain
 *  Date: 2020/6/1
 */

@BindingAdapter("imageShow")
fun setImageShow(view: ImageView, url: String?) {
    if (url.isNullOrEmpty()) {
        Glide.with(view.context).load(R.drawable.default_icon)
    } else {
        Glide.with(view.context).load(url).error(R.drawable.default_icon)
    }.into(view)
}

@BindingAdapter("viewShow")
fun setViewShow(view: View, isShowRec: Boolean) {
    Log.d("recShowTag", "view:${view},isShowRec:$isShowRec")
    view.visibility = if (isShowRec) View.VISIBLE else View.GONE
}

@BindingAdapter("textStr")
fun setTextStr(view: TextView, str: String?) {
    view.text = str ?: ""
}

@BindingAdapter("textNormalStr")
fun setTextNormalStr(view: TextView, str: String?) {
    view.text = if (str.isNullOrEmpty()) "- -" else str
}

@BindingAdapter("updateTextState")
fun setUpdateTextState(view: TextView, state: Int?) {
    view.setTextColor(
            Color.parseColor(
                    when (state) {
                        1 -> "#FF8811"
                        2, 3 -> "#959B9F"
                        else -> "#333333"
                    }
            ))
    view.text = when (state) {
        1 -> "继续答题"
        2 -> "已完成"
        3 -> "已终止"
        else -> ""
    }
}

@BindingAdapter("sexHeadImg")
fun setSexHeadImg(view: ImageView, sex: Int?) {
    view.setImageResource(if (sex != null && sex == 2) R.drawable.woman_head_img else R.drawable.test_icon)
}

@BindingAdapter("sexImageStr")
fun setSexImageStr(view: ImageView, str: Int?) {
    view.visibility = if (str == null) View.GONE else {
        view.setImageResource(if (str == 2) R.drawable.sex_woman else R.drawable.sex_man)
        View.VISIBLE
    }
}

@BindingAdapter("visitStateArrow")
fun setVisitStateArrow(view: ImageView, visitStateArrow: Int?) {
    view.setImageResource(if (visitStateArrow != null && visitStateArrow == 1) R.drawable.arrow_right_next else R.drawable.arrow_title_right)
}