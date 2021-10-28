@file:Suppress("DEPRECATION")

package com.says.common.file.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.says.common.file.enum.PushFromTypeEnum
import com.says.common.file.listener.FilePushResultListener
import com.says.common.file.model.PushFileLiveData
import java.io.File

/**
 *  Create by rain
 *  Date: 2020/12/28
 */

object FilePushCommon {
    private val fileLifeMap: MutableMap<Any, FilePushUtils> = mutableMapOf()
    fun pushFile(
        lifecycle: LifecycleOwner,
        imagePath: String?,
        isOriginal: Boolean,
        pushFromType: PushFromTypeEnum,
        weakLoading: FilePushResultListener?
    ) {
        synchronized(FilePushCommon::class) {
            var filePushModel = fileLifeMap[lifecycle]
            if (filePushModel == null) {
                filePushModel = FilePushUtils(lifecycle)
                fileLifeMap[lifecycle] = filePushModel
            }
            filePushModel.pushFile(
                initLiveData(lifecycle, weakLoading),
                imagePath,
                isOriginal,
                pushFromType
            )
        }

    }
    fun onLifeCancel(lifecycle: LifecycleOwner?) {
        if (lifecycle == null) return
        fileLifeMap.remove(lifecycle)
    }

    /**
     * 文件上传初始化
     */
    fun uploadFile(
        activity: FragmentActivity,
        imagePath: String?,
        ossPushListener: FilePushResultListener?,
        isOriginal: Boolean,
        pushFromType: PushFromTypeEnum
    ) {
        pushFile(activity, imagePath, isOriginal, pushFromType, ossPushListener)
    }

    /**
     * 文件上传初始化
     */
    fun uploadFile(
        fragment: Fragment,
        imagePath: String?,
        ossPushListener: FilePushResultListener?,
        isOriginal: Boolean,
        pushFromType: PushFromTypeEnum
    ) {
        pushFile(fragment, imagePath, isOriginal, pushFromType, ossPushListener)
    }

    private fun initLiveData(
        lifecycle: LifecycleOwner,
        weakLoading: FilePushResultListener?
    ): PushFileLiveData {
        val pushFileLiveData = PushFileLiveData()
        pushFileLiveData.bindObserver(lifecycle, {
            weakLoading?.pushProgress(it)
        }, {
            saveLog(2, it)
            weakLoading?.pushSuccess(it)
        }, {
            weakLoading?.pushFail()
        })
        return pushFileLiveData
    }


    private var logDataId: String? = null //日志上传的dataId
    private var logPatientId: String? = null //日志上传的患者id
    private var logVisitId: String? = null //日志上传的访视id
    private var logModuleId: String? = null //日志上传的模块id

    /**
     * 缓存id信息
     */
    fun saveLogIdData(dataId: String?, patientId: String?, visitId: String?, moduleId: String?) {
        logDataId = dataId
        logPatientId = patientId
        logVisitId = visitId
        logModuleId = moduleId
    }

    /**
     * 清除id信息
     */
    fun clearLogIdData() {
        logDataId = null
        logPatientId = null
        logVisitId = null
        logModuleId = null
    }

    /**
     * 行为日志上传（除非蠢）
     */
    fun saveLog(logType: Int, url: String?) {
//		BaseLoadMode.get().loadData(RetrofitFac.iData.saveSubImgLog(logPatientId ?: "",
//				logVisitId ?: "",
//				logModuleId ?: "",
//				logDataId ?: "",
//				Constants.UserCommon.getUserInfo()?.userId ?: "",
//				logType,
//				url ?: ""
//		), object : BaseLoadListener<Any> {
//			override fun fail(message: String?) {
//				L.d("logSaveTag","message:$message")
//			}
//
//			override fun success(data: Any?) {
//				L.d("logSaveTag","data:$data")
//			}
//
//			override fun subDis(d: Disposable?) {
//			}
//		})
//
    }

}