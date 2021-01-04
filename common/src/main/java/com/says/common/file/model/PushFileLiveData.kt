package com.says.common.file.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

/**
 *  Create by rain
 *  Date: 2020/12/31
 */
class PushFileLiveData {
	private val processValue = MutableLiveData<Int>()
	private var networkPathValue: String? = null
	private var pushTypeValue = MutableLiveData<Int>() //0 默认上传中，1成功，2失败
	
	
	fun postProcess(process: Int) {
		processValue.postValue(process)
	}
	
	fun postNetworkPath(value: String) {
		networkPathValue = value
		pushTypeValue.postValue(1)
	}
	
	fun postFail() {
		pushTypeValue.postValue(2)
	}
	
	fun bindObserver(lifecycle: LifecycleOwner, processBlock: (process:Int) -> Unit, successBlock: (path: String) -> Unit, failBlock: () -> Unit) {
		//进度
		processValue.observe(lifecycle, {
			if (it!=null) processBlock(it)
		})
		
		pushTypeValue.observe(lifecycle, {
			val networkPath= networkPathValue
			if (it == 1 && !networkPath.isNullOrEmpty()) {
				successBlock(networkPath)
				unBindObserver(lifecycle)
				return@observe
			}
			if (it == 2){
				failBlock()
				unBindObserver(lifecycle)
			}
		})
	}
	private fun unBindObserver(lifecycle: LifecycleOwner){
		processValue.removeObservers(lifecycle)
		pushTypeValue.removeObservers(lifecycle)
	}
}
