package com.says.common.utils

import com.google.gson.Gson

class JsonManagerHelper private constructor() {
	private var gson: Gson? = null
	
	companion object {
		private val instance: JsonManagerHelper by lazy { JsonManagerHelper() }
		
		@JvmName("create")
		@JvmStatic
		fun getHelper(): JsonManagerHelper {
			return instance
		}
	}
	
	fun getGs(): Gson {
		if (gson == null) gson = Gson()
		return gson!!
	}
	
	fun <T> strToObj(str: String?, clazz: Class<T>?): T? {
		if (str.isNullOrEmpty() || clazz == null) {
			return null
		}
		try {
			return getGs().fromJson(str, clazz)
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return null
	}
	
	fun <T> objToStr(t: T?): String? {
		if (t == null) {
			return ""
		}
		runCatching {
			getGs().toJson(t)
		}.onSuccess {
			return it
		}
		return null
	}
	
	fun beanListToStr(baseBean: MutableList<*>?): String {
		if (baseBean.isNullOrEmpty()) {
			return ""
		}
		return getGs().toJson(baseBean)
	}
	
	fun <T> dataListToStr(data: MutableList<T>?): String? {
		if (data.isNullOrEmpty()) return null
		return getGs().toJson(data)
	}
}