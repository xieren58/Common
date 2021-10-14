package com.says.common.file

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
object CommonApi {
    var END_POINT = ""
    var STS_TOKEN_URL = ""
    var bucketName: String = ""
    var objectName: String = ""
    fun initEndPoint(endPoint: String): CommonApi {
        END_POINT = endPoint
        return this
    }

    fun initStsTokenUrl(stsTokenUrl: String): CommonApi {
        STS_TOKEN_URL = stsTokenUrl
        return this
    }

    fun initBucketName(bucketName: String): CommonApi {
        CommonApi.bucketName = bucketName
        return this
    }

    fun initObjectName(objectName: String): CommonApi {
        CommonApi.objectName = objectName
        return this
    }
}