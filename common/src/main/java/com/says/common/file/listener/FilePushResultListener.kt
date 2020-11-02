package com.says.common.file.listener
import com.says.common.file.PushCommon


/**
 *  Create by rain
 *  Date: 2020/1/20
 */
interface FilePushResultListener {
	fun pushSuccess(path: String)
	fun pushFail(type: PushCommon, message: String?)
	fun pushProgress(progress: Int)
}