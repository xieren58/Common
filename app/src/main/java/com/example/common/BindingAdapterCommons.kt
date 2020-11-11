package com.example.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 *  Create by rain
 *  Date: 2020/6/1
 */

@BindingAdapter("imageShow")
fun setImageShow(view: ImageView, url: String?) {
	if (url.isNullOrEmpty()){
		Glide.with(view.context).load(R.drawable.default_icon)
	}else{
		Glide.with(view.context).load(url).error(R.drawable.default_icon)
	}.into(view)
}