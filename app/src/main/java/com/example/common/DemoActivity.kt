package com.example.common

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST
import com.rain.baselib.activity.BaseRecActivity
import com.rain.baselib.databinding.ActivityBaseRecBinding

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
class DemoActivity : BaseRecActivity<ActivityBaseRecBinding>() {
	override val viewModel by  lazy { ViewModelProvider(this).get(DemoListViewModel::class.java) }

	override fun getRecLayoutManager(): RecyclerView.LayoutManager {
		return GridLayoutManager(this,4)
	}

	override fun clickRecItem(position: Int) {
		val itemData = viewModel.getItemData(position)?:return
		if (itemData.itemType == PhotoWeightAdapter.ADD_TYPE){
			PictureUtils.onPickFromGallery(this)
		}
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == CHOOSE_REQUEST&& resultCode == RESULT_OK && data!=null){
			viewModel.addPhoto(PictureUtils.getTakeImages(data))
		}
	}
}