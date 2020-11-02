package com.example.common.http

import com.alibaba.sdk.android.oss_android_sdk.BuildConfig


/**
 *  Create by rain
 *  Date: 2020/1/16
 */
object Api {
	var BASE_URL: String
	var HOST_HOME: String
	var BASE_FOLLOW_URL: String //随访系统
	var BASE_SIGN_URL: String //签名
	var FEED_HOME: String  //意见反馈
	var VER_HOME: String
	var USER_AGREEMENT: String
	private var BASE_FOLLOW_WEB_URL: String
	var BASE_PI_WEB_URL: String
	
	
	const val BASE_OCR_TEN_CENT_URL = "https://recognition.image.myqcloud.com/"//腾讯文字识别接口
	const val BASE_OCR_ALI_URL = "https://ocrapi-advanced.taobao.com/"//阿里云文字识别接口
	const val BASE_HL_TRIAL="hltrial"//豪森
	
	init {
		if (BuildConfig.DEBUG) {
			HOST_HOME = "https://testreport.91trial.com/"
			BASE_URL = "http://test.91trial.com/api/"
			BASE_FOLLOW_URL = "http://testcore.91trial.com/"
			BASE_SIGN_URL = "https://testgateway.91trial.com/"
			BASE_FOLLOW_WEB_URL = "http://test.91trial.com/"
			BASE_PI_WEB_URL = "http://test.91trial.com/web/"
			VER_HOME = "http://demandprocess.ashermed.cn/" //更新链接
			FEED_HOME = " http://log.ashermed.com/"
			USER_AGREEMENT="https://www.91trial.com/html-fwxy/index.html?articleId="
		} else {

			if(BuildConfig.FLAVOR==BASE_HL_TRIAL){
				HOST_HOME = "https://report.huilangongyi.org.cn/"
				BASE_URL = "https://api.huilangongyi.org.cn/"
				BASE_FOLLOW_URL = "https://edc.91trial.com/"//
				BASE_SIGN_URL = "https://gateway.huilangongyi.org.cn/"
				BASE_FOLLOW_WEB_URL = "https://hltrial.huilangongyi.org.cn/"
				BASE_PI_WEB_URL = "https://hltrial.huilangongyi.org.cn/"
				VER_HOME = "http://rtms.ashermed.com/" //更新链接
				FEED_HOME = "https://log.huilangongyi.org.cn/"
				USER_AGREEMENT = "https://www.91trial.com/html-fwxy/index.html?articleId="
			}else{
				HOST_HOME = "http://report.91trial.com/"
				BASE_URL = "http://api.91trial.com/"
				BASE_FOLLOW_URL = "https://edc.91trial.com/"
				BASE_SIGN_URL = "https://gateway.91trial.com/"
				BASE_FOLLOW_WEB_URL = "https://91trial.com/"
				BASE_PI_WEB_URL = "http://www.91trial.com/"
				VER_HOME = "http://rtms.ashermed.com/" //更新链接
				FEED_HOME = " http://log.ashermed.com/"
				USER_AGREEMENT="https://www.91trial.com/html-fwxy/index.html?articleId="
			}

		}
	}
	
	
	const val WEB_TOKEN_URL = "https://token.91trial.com/"
	
	private val WEB_HOME_URL = "${HOST_HOME}vue/app"
	val WEB_HOME_CRC = WEB_HOME_URL
	val WEB_HOME_CRA = "$WEB_HOME_URL/craform"
	val WEB_HOME_PI = "$WEB_HOME_URL/piform"
	val WEB_HOME_PM = "$WEB_HOME_URL/pmform"
	
	val INDICATORS_WEB_CHART = "${BASE_FOLLOW_WEB_URL}webfp/followUpchart?patientId=%s&moduleId=%s&TimeLimit=%s"
	
	const val HEAD_URL = "https://91trial.oss-cn-shanghai.aliyuncs.com/" //阿里雲圖片地址
	const val STS_TOKEN_URL = "http://alists.ashermed.cn/api/sts"//sts应用服务器验证地址
	
	const val END_POINT = "http://oss-cn-shanghai.aliyuncs.com" //阿里云地址
	
	val REPORT_NEW_URL = "${HOST_HOME}vue/app/bullousDiseaseReport?projectId=%s&userId=%s&source=2"//随访系统报表
	val HOME_PI_REPORT_URL = "${BASE_PI_WEB_URL}Mobile/PI/PIReportList?projectId=%s&userId=%s" //pi报表
	
	val GET_VUE_DEMAND_URL: String = VER_HOME + "demand"//功能介绍
	
	
}