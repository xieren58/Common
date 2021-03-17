package com.says.common.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 *  Create by rain
 *  Date: 2020/1/7
 */
object PermissionUtils {

	/**
	 * 初始化权限回调
	 */
	@JvmStatic
	fun initResultPermission(activity: FragmentActivity,block:(MutableMap<String,Boolean>)->Unit): ActivityResultLauncher<Array<String>> {
	return 	activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
			block(it)
		}
	}
	/**
	 * 权限请求
	 */
	@JvmStatic
	fun  requestAllPermission(launcher:ActivityResultLauncher<Array<String>>,needPermissions: Array<String>){
		launcher.launch(needPermissions)
	}
	
	/**
	 * 判断权限是否已经申请
	 * 如果没有权限，尝试请求权限
	 */
	@JvmStatic
	fun requestAllPermission(activity: FragmentActivity, needPermissions: Array<String>): Boolean {
		var isHavePer = true
		if (Build.VERSION.SDK_INT >= 23 && activity.applicationInfo.targetSdkVersion >= 23) {
			for (permission in needPermissions) {
				if (!checkPermission(activity,permission)) {
					isHavePer = false
					break
				}
			}
			if (!isHavePer) requestPermission(activity, needPermissions)
		}
		return isHavePer
	}

	/**
	 * 判断权限
	 */
	@JvmStatic
	private fun checkPermission(context: Context,needPermissions: String): Boolean {
		return ContextCompat.checkSelfPermission(context, needPermissions) == PackageManager.PERMISSION_GRANTED
	}

	/**
	 * 权限回调
	 */
	@JvmStatic
	private fun requestPermission(activity: Activity, needPermissions: Array<String>) {
		ActivityCompat.requestPermissions(activity, needPermissions, 0)
	}

}