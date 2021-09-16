package com.says.common.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.says.common.CommonContext
import com.says.common.dataStore.PreferenceManager
import com.says.common.minType.MIMECommon
import com.says.common.minType.MimTypeConstants
import java.io.File
import java.math.BigDecimal
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
    @JvmStatic
    fun isTopActivity(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcessInfoList = activityManager.runningAppProcesses ?: return false
        appProcessInfoList.forEach {
            if (it.processName == context.packageName && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }

    /**
     * 判断WIFI网络是否可用
     */
    @JvmStatic
    @Suppress("DEPRECATION")
    fun isNetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= 29) {
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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
    @JvmStatic
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
            if (end == MimTypeConstants.mimArrayTypes[i][0]) type =
                MimTypeConstants.mimArrayTypes[i][1]
        }
        return type
    }

    /**
     * 用系统应用打开文件
     */
    @JvmStatic
    fun openFile(context: Context, path: String, error: () -> Unit): MIMECommon? {
        val file = File(path)
        Log.d("fileOpenTag", "file:${file.exists()}")
        if (!file.exists()) return null
        val fileType = getFileType(file.name)
        when (fileType) {
            MIMECommon.FILE_TYPE_APK -> installApk(context, path)
            else -> {
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
    @JvmStatic
    fun getUriForFile(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context.applicationContext,
                "${context.packageName}.fileProvider",
                file
            )
        } else Uri.fromFile(file)
    }


    /**
     * 获取打开文件的类型
     */
    @JvmStatic
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
            if (end == MimTypeConstants.fileArrayTypes[i][0]) type =
                MimTypeConstants.fileArrayTypes[i][1] as MIMECommon
        }
        return type
    }

    /**
     * apk安装
     */
    @JvmStatic
    fun installApk(context: Context, downloadApk: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val file = File(downloadApk)
        Log.d("downTag", "file:${file.path}")
        if (!file.exists()) return
        Log.i("downTag", "安装路径==$downloadApk")
        val apkUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
            FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", file)
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
    @JvmStatic
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
    @JvmStatic
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
    @JvmStatic
    fun isVideoUrlStr(str: String) =
        Pattern.matches(".*(.3gp|.mp4|.avi|.rm|.rmvb|.flv|.mpg|.mov|.mkv)$", str)

    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getDeviceId(): String {
        val androidId = Settings.Secure.getString(
            CommonContext.context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        if (androidId.isNullOrEmpty()) {
            return getUUID()
        }
        return androidId
    }

    /**
     * 得到全局唯一UUID
     */
    @JvmStatic
    private fun getUUID(): String {
        val mShare = PreferenceManager.getString(CommonContext.context, "sysCacheMap", "")
        var uuid = ""
        if (!mShare.isNullOrEmpty()) {
            uuid = mShare
        }
        if (uuid.isEmpty()) {
            uuid = UUID.randomUUID().toString()
            PreferenceManager.setString(CommonContext.context, "sysCacheMap", uuid)
        }
        return uuid
    }

    /**
     * 获取缓存值
     */
    fun getTotalCacheSize(): String {
        var cacheSize = getFolderSize(CommonContext.context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += getFolderSize(CommonContext.context.externalCacheDir)
        }

        cacheSize += getFileSize()
        return getFormatSize(cacheSize.toDouble())
    }

    private fun getFileSize(): Long {
        return getFileAudio()?.fold(0L) { acc, file ->
            acc + file.length()
        } ?: 0L
    }

    private fun getFileAudio(): MutableList<File>? {
        try {
            val externalFilesDir = CommonContext.context.getExternalFilesDir(Context.AUDIO_SERVICE)
                ?: return null
            return externalFilesDir.listFiles()?.filter {
                it != null && it.exists() && it.isFile
            }?.toMutableList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 格式化单位
     */
    private fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        val megaByte = kiloByte / 1024
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB"
        }
        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB"
        }
        val result4 = BigDecimal.valueOf(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    /**
     * 获取文件
     */
    private fun getFolderSize(file: File?): Long {
        var size: Long = 0
        if (file != null) {
            val fileList = file.listFiles()
            if (fileList != null && fileList.isNotEmpty()) {
                for (i in fileList.indices) {
                    // 如果下面还有文件
                    size += if (fileList[i].isDirectory) {
                        getFolderSize(fileList[i])
                    } else {
                        fileList[i].length()
                    }
                }
            }
        }
        return size
    }

}