package com.says.common.file.download

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope


/**
 *  Create by rain
 *  Date: 2021/10/28
 */
object DownloadFileManager {
    private val downloadList = mutableListOf<DownloadCommon>()
    /**
     * 获取所有下载任务
     */
    @JvmStatic
    fun getList() = downloadList

    /**
     * 新增下载任务
     */
    @JvmStatic
    fun addList(common: DownloadCommon) {
        downloadList.add(common)
    }
    /**
     * 删除缓存的下载任务
     */
    @JvmStatic
    fun removeList(common: DownloadCommon) {
        downloadList.remove(common)
    }

    /**
     * 取消下载任务
     */
    @JvmStatic
    fun cancelJob(url: String) {
        val list = downloadList.filter {
            it.builder.getUrl() == url
        }
        list.forEach {
            it.cancel()
        }
        downloadList.removeAll(list)
    }

    @JvmStatic
    fun initBuilder(activity: FragmentActivity): DownloadBuilder {
        return DownloadBuilder(activity.lifecycleScope)
    }

    @JvmStatic
    @DelicateCoroutinesApi
    fun initBuilder(context: Context): DownloadBuilder {
        return DownloadBuilder(if (context is FragmentActivity) context.lifecycleScope else GlobalScope)
    }

    @JvmStatic
    fun initBuilder(viewModel: ViewModel): DownloadBuilder {
        return DownloadBuilder(viewModel.viewModelScope)
    }

    @JvmStatic
    fun initBuilder(fragment: Fragment): DownloadBuilder {
        return DownloadBuilder(fragment.lifecycleScope)
    }
}
