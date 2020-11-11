package com.says.common

import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.says.common.minType.MIMECommon
import com.says.common.minType.MimTypeConstants
import java.io.File
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern


/**
 *  Create by rain
 *  Date: 2020/11/2
 */
object Common {


	/**
	 * 应用是否在上层
	 */
	fun isTopActivity(context: Context): Boolean {
		val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
		val appProcessInfoList = activityManager.runningAppProcesses ?: return false
		appProcessInfoList.forEach {
			if (it.processName ==  context.packageName && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true
			}
		}
		return false
	}
	
	/**
	 * 判断WIFI网络是否可用
	 */
	@Suppress("DEPRECATION")
	fun isNetConnected(context: Context): Boolean {
		val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		if (Build.VERSION.SDK_INT >= 29) {
			val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
							?: return false
			return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
		} else {
			val activeNetworkInfo = connectivityManager.activeNetworkInfo
			return activeNetworkInfo != null && activeNetworkInfo.isConnected
		}
	}


	/**
	 * 根据文件后缀名获得对应的MIME类型
	 * 文件名，需要包含后缀.xml类似这样的
	 */
	 fun getMIMEType(fileName: String): String {
		var type = "*/*"
		//获取后缀名前的分隔符"."在fName中的位置。
		val dotIndex = fileName.lastIndexOf(".")
		if (dotIndex < 0) {
			return type
		}
		/* 获取文件的后缀名*/
		val end = fileName.substring(dotIndex, fileName.length).toLowerCase(Locale.getDefault())
		if (end === "") return type
		//在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (i in MimTypeConstants.mimArrayTypes.indices) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
			if (end == MimTypeConstants.mimArrayTypes[i][0]) type = MimTypeConstants.mimArrayTypes[i][1]
		}
		return type
	}

	/**
	 * 用系统应用打开文件
	 */
	fun openFile(context: Context, path: String,error:()->Unit) :MIMECommon?{
		val file = File(path)
		Log.d("fileOpenTag", "file:${file.exists()}")
		if (!file.exists())return null
		val fileType = getFileType(file.name)
		when(fileType){
			MIMECommon.FILE_TYPE_APK->installApk(context,path)
			else->{
				try {
					val intent = Intent().apply {
						addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
						action = Intent.ACTION_VIEW
						val mimeType = getMIMEType(file.name)
						Log.d("fileOpenTag", "mimeType:$mimeType")
						setDataAndType(getUriForFile(context, file), mimeType)
					}
					context.startActivity(intent)
					Intent.createChooser(intent, "请选择对应的软件打开该附件！")
				} catch (e: ActivityNotFoundException) {
					error()
				}
			}
		}
		return fileType
	}

	/**
	 * 獲取文件的uri
	 */
	 fun getUriForFile(context: Context, file: File): Uri {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			FileProvider.getUriForFile(context.applicationContext, "${context.packageName}.fileProvider", file)
		} else Uri.fromFile(file)
	}


	/**
	 * 获取打开文件的类型
	 */
	fun getFileType(fileName: String): MIMECommon {
		var type = MIMECommon.FILE_TYPE_OTHER
		//获取后缀名前的分隔符"."在fName中的位置。
		val dotIndex = fileName.lastIndexOf(".")
		if (dotIndex < 0) {
			return type
		}
		/* 获取文件的后缀名*/
		val end = fileName.substring(dotIndex, fileName.length).toLowerCase(Locale.getDefault())
		if (end === "") return type
		for (i in MimTypeConstants.fileArrayTypes.indices) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
			if (end == MimTypeConstants.fileArrayTypes[i][0]) type = MimTypeConstants.fileArrayTypes[i][1] as MIMECommon
		}
		return type
	}

	/**
	 * apk安装
	 */
	fun installApk(context: Context,downloadApk: String) {
		val intent = Intent(Intent.ACTION_VIEW)
		val file = File(downloadApk)
		Log.d("downTag", "file:${file.path}")
		if (!file.exists()) return
		Log.i("downTag","安装路径==$downloadApk")
		val apkUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
			FileProvider.getUriForFile(context, "${context.packageName }.fileProvider", file)
		} else {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			Uri.fromFile(file)
		}
		intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
		context.startActivity(intent)
	}
	
	/**
	 * 移动到recyclerview的最后一位
	 */
	fun moveToPosition(mRecyclerView: RecyclerView?, n: Int) {
		if (mRecyclerView == null) return
		val layoutManager = mRecyclerView.layoutManager as LinearLayoutManager? ?: return
		val firstItem = layoutManager.findFirstVisibleItemPosition()
		val lastItem = layoutManager.findLastVisibleItemPosition()
		when {
			n <= firstItem -> mRecyclerView.scrollToPosition(n)
			n <= lastItem -> {
				val top = mRecyclerView.getChildAt(n - firstItem).top
				mRecyclerView.scrollBy(0, top)
			}
			else -> mRecyclerView.scrollToPosition(n)
		}
	}
	
	
	/**
	 * 转换文件大小
	 *
	 * @param fileS
	 * @return
	 */
	fun formatFileSize(fileS: Long): String {
		val df = DecimalFormat("#.00")
		val wrongSize = "0B"
		if (fileS == 0L) return wrongSize
		return when {
			fileS < 1024 -> df.format(fileS.toDouble()) + "B"
			fileS < 1048576 -> df.format(fileS.toDouble() / 1024) + "KB"
			fileS < 1073741824 -> df.format(fileS.toDouble() / 1048576) + "MB"
			else -> df.format(fileS.toDouble() / 1073741824) + "GB"
		}
	}

	/**
	 * 判断是否是视频文件
	 */
	fun isVideoUrlStr(str: String) =
		Pattern.matches(".*(.3gp|.mp4|.avi|.rm|.rmvb|.flv|.mpg|.mov|.mkv)$", str)
	
}