package com.rain.baselib.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rain.baselib.R
import com.rain.baselib.viewModel.BaseViewModel

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
abstract class BaseActivity :AppCompatActivity(){
    @get:LayoutRes
    protected abstract val layoutResId: Int //佈局id
    protected open val variableId: Int = -1 //佈局内的id设置null代表不需要dataBind，可随意输入T泛型
    private var dataBind: ViewDataBinding? = null

    protected abstract val viewModel: BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stateBarTextColor()
        dataBind = DataBindingUtil.setContentView(this, layoutResId)
        initViewDataBinding()
        initIntent()
        init(savedInstanceState)
    }

    private fun initViewDataBinding() {
        viewModel?.setLoadDialogObserve(this, {
            if (it == null) return@setLoadDialogObserve
            if (it) showDialogLoad() else dismissDialogLoad()
        })
        if (variableId != -1 && viewModel != null)	dataBind?.setVariable(variableId, viewModel)
        dataBind?.lifecycleOwner = this

    }


    @CallSuper
    open fun init(savedInstanceState: Bundle?) {
        initView()
        initEvent()
        initModelObserve()
        viewModel?.initModel()
        initData()
    }

    open fun initModelObserve() = Unit
    open fun initIntent() = Unit
    open fun initEvent() = Unit
    open fun initView() = Unit
    open fun initData() = Unit

    @Suppress("DEPRECATION")
    private fun stateBarTextColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && whiteStateBarText) window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    //	open fun isWhiteStateBarText() = true
    open val whiteStateBarText = true

    /**
     * 设置toolbar的颜色
     */

    fun setBarColor(@ColorRes color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) window.statusBarColor =
            ContextCompat.getColor(this, color)
    }

    private var loading: AlertDialog? = null

    /**
     * 打开loading
     */
    @SuppressLint("InflateParams")
    fun showDialogLoad() {
        try {
            if (loading == null) loading = AlertDialog.Builder(this).create()
            val window = loading?.window
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            loading?.setCanceledOnTouchOutside(false)
            if (!loading!!.isShowing) {
                loading?.show()
                loading?.setContentView(
                    LayoutInflater.from(this).inflate(R.layout.layout_loading_dialog, null)
                )
            } else {
                loading?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 关闭loading
     */
    fun dismissDialogLoad() {
        try {
            if (loading != null && loading?.isShowing!!) loading?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}