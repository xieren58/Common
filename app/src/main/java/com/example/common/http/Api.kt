package com.example.common.http

import android.util.Log
import com.example.common.MyApp
import java.util.*


/**
 *  Create by rain
 *  Date: 2020/1/16
 */
object Api {
	var BASE_URL = ""
	
	init {
		val properties = Properties()
		val open = MyApp.context.assets.open("url.properties")
//		Log.d("ApiPropertiesTag","open:$open")
//		val resource = Api.javaClass.classLoader?.getResource("url.properties")
		Log.d("ApiPropertiesTag", "classLoader:${ Api.javaClass.classLoader}")
		Log.d("ApiPropertiesTag", "getResource:${ 	Api.javaClass.getResource("url.properties")}")
		val resourceAsStream = Api.javaClass.getResourceAsStream("url.properties")
		Log.d("ApiPropertiesTag", "resourceAsStream:${ resourceAsStream}")
		properties.load(open)
		BASE_URL = properties.getProperty("BASE_URL")
		
		Log.d("ApiPropertiesTag", "BASE_URL:$BASE_URL")
	}
}