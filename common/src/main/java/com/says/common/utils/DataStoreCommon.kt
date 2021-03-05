@file:Suppress("UNCHECKED_CAST")

package com.says.common.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.says.common.CommonContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

/**
 *  Create by rain
 *  Date: 2021/3/4
 */

object DataStoreCommon {
    /**
     * 委托的方式创建存储dataStore
     */
    private val Context.dataStore by preferencesDataStore(name = "${CommonContext.context.packageName}_store")

    /**
     * 懒加载初始化dataStore
     */
    private val dataPre by lazy { CommonContext.context.dataStore }

    /**
     * 获取string类型的值
     */
    suspend fun getString(key: String, defaultValue: String? = null): String? {
        return dataPre.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }.firstOrNull()
    }

    /**
     * 获取set[String]类型的值
     */
    suspend fun getStringSet(key: String, defaultValue: Set<String>? = null): Set<String>? {
        return dataPre.data.map { preferences ->
            preferences[stringSetPreferencesKey(key)] ?: defaultValue
        }.firstOrNull()
    }


    /**
     * 获取其他类型的值
     */
    suspend fun <T : Any> getOtherValue(key: String, defaultValue: T): T {
        val pKey = when (defaultValue::class) {
            String::class -> stringPreferencesKey(key)
            Int::class -> intPreferencesKey(key)
            Double::class -> doublePreferencesKey(key)
            Boolean::class -> booleanPreferencesKey(key)
            Float::class -> floatPreferencesKey(key)
            Long::class -> longPreferencesKey(key)
            else -> null
        }
        return dataPre.data.map { preferences ->
            if (pKey != null) {
                (preferences[pKey] as? T) ?: defaultValue
            } else defaultValue
        }.firstOrNull() ?: defaultValue
    }

    /**
     * 设置值根据类型设置
     */
    suspend fun putValue(key: String, value: Any) {
        dataPre.edit { settings ->
            when (value::class) {
                String::class -> settings[stringPreferencesKey(key)] = value as String
                Int::class -> settings[intPreferencesKey(key)] = value as Int
                Double::class -> settings[doublePreferencesKey(key)] = value as Double
                Boolean::class -> settings[booleanPreferencesKey(key)] = value as Boolean
                Float::class -> settings[floatPreferencesKey(key)] = value as Float
                Long::class -> settings[longPreferencesKey(key)] = value as Long
                else -> settings[stringPreferencesKey(key)] = value.toString()
            }
        }
    }

    /**
     * 批量存储
     */
    suspend fun putValue(vararg params: Pair<String, Any>) {
        if (params.isNullOrEmpty()) return
        dataPre.edit { settings ->
            params.forEach {
                val first = it.first
                val second = it.second
                Log.d("dataStoreTag", "first:$first,second:$second")
                when (second::class) {
                    String::class -> settings[stringPreferencesKey(first)] = second as String
                    Int::class -> settings[intPreferencesKey(first)] = second as Int
                    Double::class -> settings[doublePreferencesKey(first)] = second as Double
                    Boolean::class -> settings[booleanPreferencesKey(first)] = second as Boolean
                    Float::class -> settings[floatPreferencesKey(first)] = second as Float
                    Long::class -> settings[longPreferencesKey(first)] = second as Long
                    else -> settings[stringPreferencesKey(first)] = second.toString()
                }
            }
        }
    }
}


