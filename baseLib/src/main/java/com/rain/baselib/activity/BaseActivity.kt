package com.rain.baselib.activity

import android.annotation.SuppressLint
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
import androidx.viewbinding.ViewBinding
import com.rain.baselib.R
import com.rain.baselib.common.conversionViewBind
import com.rain.baselib.viewModel.BaseViewModel

/**
 * base基类 - t为[ViewBinding]。可输入[ViewDataBinding]，[ViewDataBinding]情况下绑定[BaseViewModel]。
 * 根据viewBind自动设置布局
 */
abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    protected open val variableId: Int = -1 //佈局内的id设置null代表不需要dataBind

    protected lateinit var viewBind: T
    protected abstract val viewModel: BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stateBarTextColor()
        viewBind = conversionViewBind()
        setContentView(viewBind.root)
        if (viewBind is ViewDataBinding) DataBindingUtil.bind<ViewDataBinding>(viewBind.root)
        initViewDataBinding()
        initIntent()
        init(savedInstanceState)
    }

    private fun initViewDataBinding() {
        viewModel?.setLoadDialogObserve(this, {
            if (it == null) return@setLoadDialogObserve
            if (it) showDialogLoad() else dismissDialogLoad()
        })
        (viewBind as? ViewDataBinding)?.run {
            if (variableId != -1 && viewModel != null) setVariable(variableId, viewModel)
            lifecycleOwner = this@BaseActivity
        }
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