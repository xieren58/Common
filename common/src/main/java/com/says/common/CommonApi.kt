package com.says.common

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
object CommonApi {
	var END_POINT =""
	var STS_TOKEN_URL =""
	var bucketName:String=""
	var objectName:String=""
	fun initEndPoint(endPoint: String):CommonApi {
		this.END_POINT = endPoint
		return  this
	}
	fun initStsTokenUrl(stsTokenUrl: String):CommonApi {
		this.STS_TOKEN_URL = stsTokenUrl
		return  this
	}
	fun initBucketName(bucketName: String):CommonApi {
		this.bucketName = bucketName
		return  this
	}
	fun initObjectName(objectName: String):CommonApi {
		this.objectName = objectName
		return  this
	}
}