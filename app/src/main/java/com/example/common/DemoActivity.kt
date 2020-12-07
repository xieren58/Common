package com.example.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.rain.baselib.activity.BaseRecActivity
import com.rain.baselib.databinding.ActivityBaseRecBinding
import com.says.common.utils.JsonManagerHelper

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
class DemoActivity : BaseRecActivity<ActivityBaseRecBinding, DemoListViewModel>() {
	override fun getRecLayoutManager() = GridLayoutManager(this, 4)
	override val loadRefreshEnable: Boolean
		get() = false
	override val loadMoreEnable: Boolean
		get() = false
	
	override fun clickRecItem(position: Int) {
		val itemData = viewModel.getItemData(position) ?: return
		if (itemData.itemType == PhotoWeightAdapter.ADD_TYPE) {
			PictureUtils.onPickFromGallery(this) {
				if (!it.isNullOrEmpty()) viewModel.addPhoto(it)
			}
		}
	}
	
	override fun getRightStr(): String? {
		return "确认"
	}
	
	override fun rightTvClick() {
		setResult(RESULT_OK, Intent().apply {
			putExtra("demo",JsonManagerHelper.getHelper().objToStr(viewModel.getLists()))
		})
		finish()
	}
	
	override fun initIntent(savedInstanceState: Bundle?) {
		super.initIntent(savedInstanceState)
		val requestData = intent?.getStringExtra("requestData")
		Log.d("resultTag", "requestData:$requestData")
	}
}