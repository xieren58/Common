package com.says.common.file.download

/**
 *  Create by rain
 *  Date: 2021/10/28
 */
interface DownloadFileListener {
    fun onSuccess(path: String)

    fun onError(message: String)

    fun onRetry(count: Int)

    fun onProcess(process: Int)

    fun onRepeat() {}

    fun onBefore() {}
}