package com.rain.baselib.viewModel

import androidx.lifecycle.MutableLiveData
import com.rain.baselib.common.NetWorkUtils

/**
 *  Create by rain
 *  Date: 2020/8/14
 */
class BaseWebViewModel : BaseViewModel() {
    val proShow = MutableLiveData(true)
    val isShowWeb = MutableLiveData(true)
    val url = MutableLiveData<String>()
    val webPro = MutableLiveData<Int>()
    fun loadUrl(webUrl: String?) {
        if (!NetWorkUtils.isNetConnected()) {
            showErr()
            return
        }
        showWeb()
        url.value = webUrl
    }

    private fun showErr() {
        proShow.value = false
        isShowWeb.value = false
    }

    private fun showWeb() {
        proShow.value = true
        isShowWeb.value = true
    }

    fun setPro(newProgress: Int) {
        if (newProgress >= 99) {
            webPro.value = 100
            proShow.value = false
        } else {
            proShow.value = true
            webPro.value = newProgress
        }
    }
}