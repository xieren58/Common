package com.says.common.file.listener


/**
 *  Create by rain
 *  Date: 2020/1/20
 */
interface FilePushResultListener {
	fun pushSuccess(path: String)
	fun pushFail()
	fun pushProgress(progress: Int)
}