package com.example.common

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout.HORIZONTAL
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.says.common.ui.UICommon
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_demo.*

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
class DemoActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_demo)
		initView()
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == PictureConfig.CHOOSE_REQUEST) {
			if (resultCode == RESULT_OK && data != null) {
				val takeImages = PictureUtils.getTakeImages(data)
				if (takeImages.isNullOrEmpty())return
				val updatePicList :MutableList<UpdatePic> = mutableListOf()
				takeImages.forEach {
					updatePicList.add(UpdatePic(it))
				}
				adapter.addItemData(updatePicList)
			}
		}
	}
	private val adapter by lazy { PhotoWeightAdapter() }
	private fun initView(){
		rec_bottom.layoutManager = LinearLayoutManager(this)
		rec_bottom?.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).size(10).colorResId(R.color.teal_200).build())
		adapter.run {
			setPhotoItemClickListener(object : PhotoWeightAdapter.PhotoItemClickListener {
				override fun itemAdd() {
					PictureUtils.onPickFromGallery(this@DemoActivity,isSingle = false,maxSize = 9)
				}
				
				override fun itemClick(position: Int) {
					Log.d("photoClickTag","position:$position")
				}
				
				override fun itemDelete(position: Int) {
					adapter.removeItemData(position)
				}
			})
			rec_bottom.adapter = this
		}
		adapter.setData(null)
	}
}