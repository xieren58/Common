package com.says.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import okhttp3.internal.ignoreIoExceptions

/**
 *  Create by rain
 *  Date: 2020/1/7
 */
object PermissionUtils {
    
    /**
     * 初始化权限回调
     */
    @JvmStatic
    fun initResultPermission(
        activity: FragmentActivity,
        block: (MutableMap<String, Boolean>) -> Unit
    ): ActivityResultLauncher<Array<String>> {
        return activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            block(it)
        }
    }
    
    /**
     * 权限请求
     */
    @JvmStatic
    fun requestAllPermission(
        launcher: ActivityResultLauncher<Array<String>>,
        needPermissions: Array<String>
    ) {
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
                if (!checkPermission(activity, permission)) {
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
    fun checkPermissionPermission(context: Context,vararg needPermissions: String): Boolean {
        needPermissions.forEach {
            if (!checkPermission(context,it))return false
        }
        return true
    }
    /**
     * 判断权限
     */
    @JvmStatic
    fun checkPermission(context: Context, needPermissions: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            needPermissions
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * 权限回调
     */
    @JvmStatic
    private fun requestPermission(activity: Activity, needPermissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, needPermissions, 0)
    }
    
    /**
     * 检查白名单权限
     */
    fun checkIgnoreBatteryOptimizations(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val systemService = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
            systemService?.isIgnoringBatteryOptimizations(context.packageName) ?: false
        } else true
    }
    
    /**
     * 获取白名单权限
     */
    @SuppressLint("BatteryLife")
    fun requestIgnoreBatteryOptimizations(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                activity.startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:${activity.packageName}")
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 跳转到指定应用的首页
     */
    @JvmStatic
    private fun showActivity(context: Context, packageName: String): Boolean {
        return try {
            val intent =
                context.packageManager.getLaunchIntentForPackage(packageName) ?: return false
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 跳转到指定应用的指定页面
     */
    @JvmStatic
    private fun showActivity(context: Context, packageName: String, activityDir: String): Boolean {
        
        return try {
            context.startActivity(Intent().apply {
                component = ComponentName(packageName, activityDir)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            true
        } catch (e: Exception) {
            Log.e("batteryTag", "showActivity-e:$e")
            false
        }
    }
    
    
    /**
     * 厂家判断
     */
    @JvmStatic
    fun isHuawei(): Boolean {
        return Build.BRAND != null && (Build.BRAND.equals(
            "huawei",
            true
        ) || Build.BRAND.equals("honor", true))
    }
    
    @JvmStatic
    fun isXiaomi(): Boolean {
        return Build.BRAND != null && Build.BRAND.equals("xiaomi", true)
    }
    
    @JvmStatic
    fun isOPPO(): Boolean {
        return Build.BRAND != null && Build.BRAND.equals("oppo", true)
    }
    
    @JvmStatic
    fun isViVo(): Boolean {
        return Build.BRAND != null && Build.BRAND.equals("vivo", true)
    }
    
    @JvmStatic
    fun isMeiZu(): Boolean {
        return Build.BRAND != null && Build.BRAND.equals("meizu", true)
    }
    
    @JvmStatic
    fun isSamsung(): Boolean {
        return Build.BRAND != null && Build.BRAND.equals("samsung", true)
    }
    
    @JvmStatic
    fun isLeTV(): Boolean {
        return Build.BRAND != null && Build.BRAND.equals("letv", true)
    }
    
    @JvmStatic
    fun isSmartIsAn(): Boolean {
        return Build.BRAND != null && Build.BRAND.equals("smartisan", true)
    }
    
    @JvmStatic
    fun openBatterySettings(context: FragmentActivity): Boolean {
        Log.d("batteryTag", "openBatterySettings")
        Log.d("batteryTag", "BRAND:${Build.BRAND}")
        
        return when {
            isHuawei() -> goHuaweiSetting(context)
            isXiaomi() -> goXiaomiSetting(context)
            isOPPO() -> goOPPOSetting(context)
            isViVo() -> goViVoSetting(context)
            isMeiZu() -> goMeiZuSetting(context)
            isSamsung() -> goSamsungSetting(context)
            isLeTV() -> goLeTvSetting(context)
            isSmartIsAn() -> goSmartIsAnSetting(context)
            else -> {
                false
            }
        }
    }
    
    /**
     * 跳转到手机管家
     */
    @JvmStatic
    fun goHuaweiSetting(context: FragmentActivity): Boolean {
//		val requestAllPermission = requestAllPermission(
//			context,
//			arrayOf("com.huawei.permission.external_app_settings.USE_COMPONENT")
//		)
//		Log.d("batteryTag","requestAllPermission:$requestAllPermission")
//		if (!requestAllPermission)return false
        return try {
            val isOpen = showActivity(
                context,
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
            )
            if (!isOpen) {
                showActivity(
                    context,
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity"
                )
            } else true
        } catch (e: java.lang.Exception) {
            Log.e("batteryTag", "e:$e")
            showActivity(
                context,
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.optimize.bootstart.BootStartActivity"
            )
        }
    }
    
    @JvmStatic
    fun goXiaomiSetting(context: Context): Boolean {
        return showActivity(
            context,
            "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        )
    }
    
    @JvmStatic
    fun goOPPOSetting(context: Context): Boolean {
        return try {
            showActivity(context, "com.coloros.phonemanager")
        } catch (e1: java.lang.Exception) {
            try {
                showActivity(context, "com.oppo.safe")
            } catch (e2: java.lang.Exception) {
                try {
                    showActivity(context, "com.coloros.oppoguardelf")
                } catch (e3: java.lang.Exception) {
                    showActivity(context, "com.coloros.safecenter")
                }
            }
        }
    }
    
    @JvmStatic
    fun goViVoSetting(context: Context): Boolean {
        return showActivity(context, "com.iqoo.secure")
    }
    
    @JvmStatic
    fun goMeiZuSetting(context: Context): Boolean {
        return showActivity(context, "com.meizu.safe")
    }
    
    @JvmStatic
    fun goSamsungSetting(context: Context): Boolean {
        return try {
            showActivity(context, "com.samsung.android.sm_cn")
        } catch (e: java.lang.Exception) {
            showActivity(context, "com.samsung.android.sm")
        }
    }
    
    @JvmStatic
    fun goLeTvSetting(context: Context): Boolean {
        return showActivity(
            context,
            "com.letv.android.letvsafe",
            "com.letv.android.letvsafe.AutobootManageActivity"
        )
    }
    
    @JvmStatic
    fun goSmartIsAnSetting(context: Context): Boolean {
        return showActivity(context, "com.smartisanos.security")
    }
    
}