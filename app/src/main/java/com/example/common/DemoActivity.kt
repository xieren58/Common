package com.example.common

import androidx.recyclerview.widget.GridLayoutManager
import com.rain.baselib.activity.BaseRecActivity
import com.rain.baselib.databinding.ActivityBaseRecBinding

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
class DemoActivity : BaseRecActivity<ActivityBaseRecBinding,DemoListViewModel>() {
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
}