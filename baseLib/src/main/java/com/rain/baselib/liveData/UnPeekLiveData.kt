package com.rain.baselib.liveData

/**
 *  Create by rain
 *  Date: 2021/3/12
 */
class UnPeekLiveData<T> : ProtectedUnPeekLiveData<T>() {
	public override fun setValue(value: T?) {
		super.setValue(value)F
	}
	
	public override fun postValue(value: T?) {
		super.postValue(value)
	}
}
