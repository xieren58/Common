package com.example.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.luck.picture.lib.config.PictureConfig
import com.says.common.file.PushCommon
import com.says.common.file.PushFileManager
import com.says.common.file.listener.FilePushResultListener
import com.says.common.startAc
import com.says.common.ui.singleClick
import kotlinx.android.synthetic.main.activity_demo.*

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
class DemoActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_demo)
		tv_start.singleClick {
			PictureUtils.onPickFromAll(this)
		}
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == PictureConfig.CHOOSE_REQUEST) {
			if (resultCode == RESULT_OK && data != null) {
				PictureUtils.getTakeImages(data) {
					if (it.isNullOrEmpty()) {
						return@getTakeImages
					}
					pushImage(it[0])
				}
				
			}
		}
	}
	
	private fun pushImage(imagePath: String) {
		Glide.with(this).load(imagePath).into(ig_image)
		PushFileManager.init(this).get().original(true).setListener(object : FilePushResultListener {
			override fun pushSuccess(path: String) {
				tv_process.text = "完成"
			}
			
			override fun pushFail(type: PushCommon, message: String?) {
				tv_process.text = "失败"
			}
			
			override fun pushProgress(progress: Int) {
				tv_process.text = progress.toString()
			}
		}).build(imagePath)
	}
}