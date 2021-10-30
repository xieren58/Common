package com.says.common.file.download

import android.content.Context
import kotlinx.coroutines.CoroutineScope


/**
 * 文件下载参数构建器
 */
class DownloadBuilder(private val scope: CoroutineScope) {
    private var mUrl: String? = null //下载地址
    private var mListener: DownloadFileListener? = null //下载监听
    private var mSavePath: String? = null // 保存的路徑
    private var mRetryMaxCount = 3 //最大重试次数
    private var mFileName: String? = null // 文件名称
    private var mMd5Str: String? = null //需要校验的md5
    private var mIsRepeat: Boolean = true //是否可以重复下载
    fun repeat(isRepeat: Boolean): DownloadBuilder {
        this.mIsRepeat = isRepeat
        return this
    }

    /**
     * 地址
     */
    fun url(url: String): DownloadBuilder {
        this.mUrl = url
        return this
    }

    fun listener(listener: DownloadFileListener): DownloadBuilder {
        this.mListener = listener
        return this
    }

    fun path(savePath: String): DownloadBuilder {
        this.mSavePath = savePath
        return this
    }

    fun retryMaxCount(count: Int): DownloadBuilder {
        this.mRetryMaxCount = count
        return this
    }

    fun fileName(fileName: String): DownloadBuilder {
        this.mFileName = fileName
        return this
    }

    fun md5(str: String): DownloadBuilder {
        this.mMd5Str = str
        return this
    }

    fun build() {
        if (!mIsRepeat) {
            val list = DownloadFileManager.getList().filter {
                it.builder.getUrl() == mUrl
            }
            if (!list.isNullOrEmpty()) {
                this.mListener?.onRepeat()
                return
            }
        }
        val downloadCommon = DownloadCommon(this)
        DownloadFileManager.addList(downloadCommon)
        downloadCommon.download(scope)
    }

    fun getRepeat() = mIsRepeat
    fun getUrl() = mUrl
    fun getListener() = mListener
    fun getSavePath() = mSavePath
    fun getRetryMaxCount() = mRetryMaxCount
    fun getFileName() = mFileName
    fun getMd5() = mMd5Str
}