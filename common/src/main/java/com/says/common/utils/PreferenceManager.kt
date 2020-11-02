package com.says.common.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private fun getSharedPreference(context:Context): SharedPreferences {
        return context.getSharedPreferences("${context.packageName}_sp", Context.MODE_PRIVATE)
    }


    fun getString(context:Context,key: String, defValue: String? =null): String? {
        return getSharedPreference(context).getString(key, defValue)
    }

    fun getInt(context:Context,key: String, defValue: Int = -1): Int {
        return getSharedPreference(context).getInt(key, defValue)
    }

    fun getLong(context:Context,key: String, defValue: Long = -1): Long {
        return getSharedPreference(context).getLong(key, defValue)
    }

    fun getBoolean(context:Context,key: String, defValue: Boolean = false): Boolean {
        return getSharedPreference(context).getBoolean(key, defValue)
    }

    fun getFloat(context:Context,key: String, defValue: Float = -1F): Float {
        return getSharedPreference(context).getFloat(key, defValue)
    }

    fun setString(context:Context,key: String, value: String) {
        getSharedPreference(context).edit().putString(key, value).apply()
    }

    fun setInt(context:Context,key: String, value: Int) {
        getSharedPreference(context).edit().putInt(key, value).apply()
    }

    fun setLong(context:Context,key: String, value: Long) {
        getSharedPreference(context).edit().putLong(key, value).apply()
    }

    fun setFloat(context:Context,key: String, value: Float) {
        getSharedPreference(context).edit().putFloat(key, value).apply()
    }

    fun setBoolean(context:Context,key: String, value: Boolean) {
        getSharedPreference(context).edit().putBoolean(key, value).apply()
    }

    fun setStringSet(context:Context,key: String, value: Set<String>) {
        getSharedPreference(context).edit().putStringSet(key, value).apply()
    }

    fun getStringSet(context:Context,key: String, value: Set<String>): Set<String>? {
        return getSharedPreference(context).getStringSet(key, value)
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    fun clear(context:Context) {
        getSharedPreference(context).edit().clear().apply()
    }
}