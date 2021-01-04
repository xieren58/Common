@file:Suppress("DEPRECATION")

package com.says.common.file.utils

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.says.common.file.enum.PushFromTypeEnum
import com.says.common.file.listener.FilePushResultListener
import com.says.common.file.model.PushFileLiveData

/**
 *  Create by rain
 *  Date: 2020/12/28
 */

object FilePushManager {
	private val fileLifeMap: MutableMap<Any, FilePushUtils> = mutableMapOf()
	private fun pushFile(lifecycle: LifecycleOwner, imagePath: String?, isOriginal: Boolean, pushFromType: PushFromTypeEnum, weakLoading: FilePushResultListener?) {
		synchronized(FilePushManager::class) {
			Log.d("filePushTag", "lifecycle:$lifecycle")
			var filePushModel = fileLifeMap[lifecycle]
			if (filePushModel == null) {
				filePushModel = FilePushUtils(lifecycle)
				fileLifeMap[lifecycle] = filePushModel
			}
			Log.d("filePushTag", "filePushModel:$filePushModel")
			filePushModel.pushFile(initLiveData(lifecycle, weakLoading), imagePath, isOriginal, pushFromType)
		}
		
	}
	
	fun onLifeCancel(lifecycle: LifecycleOwner?) {
		if (lifecycle == null) return
		fileLifeMap.remove(lifecycle)
	}
	
	/**
	 * 文件上传初始化
	 */
	fun uploadFile(activity: FragmentActivity, imagePath: String?, ossPushListener: FilePushResultListener?, isOriginal: Boolean, pushFromType: PushFromTypeEnum) {
		pushFile(activity, imagePath, isOriginal, pushFromType, ossPushListener)
	}
	
	/**
	 * 文件上传初始化
	 */
	fun uploadFile(fragment: Fragment, imagePath: String?, ossPushListener: FilePushResultListener?, isOriginal: Boolean, pushFromType: PushFromTypeEnum) {
		pushFile(fragment, imagePath, isOriginal, pushFromType, ossPushListener)
	}
	
	private fun initLiveData(lifecycle: LifecycleOwner, weakLoading: FilePushResultListener?): PushFileLiveData {
		val pushFileLiveData = PushFileLiveData()
		pushFileLiveData.bindObserver(lifecycle, {
			Log.d("liveDataTag", "pushProcess:$it")
			weakLoading?.pushProgress(it)
		}, {
			Log.d("liveDataTag", "pushSuccess:$it")
			weakLoading?.pushSuccess(it)
		}, {
			Log.d("liveDataTag", "上传失败")
			weakLoading?.pushFail()
		})
		return pushFileLiveData
	}
	
}