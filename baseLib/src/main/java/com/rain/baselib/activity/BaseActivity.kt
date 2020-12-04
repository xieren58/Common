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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.rain.baselib.R
import com.rain.baselib.common.conversionViewBind
import com.rain.baselib.common.conversionViewModel
import com.rain.baselib.viewModel.BaseViewModel

/**
 * base基类 - T为[ViewBinding]。可输入[ViewDataBinding]，[ViewDataBinding]情况下绑定[BaseViewModel]。
 * VM为[BaseViewModel] 根据泛型类型，自动获取viewModel对象，并在[variableId]不为-1的情况下进行[ViewDataBinding]和[BaseViewModel]的绑定
 * 在子类不需要viewModel时，泛型传入[BaseViewModel]即可
 * 根据viewBind自动设置布局
 */
abstract class BaseActivity<T : ViewBinding,VM:BaseViewModel> : AppCompatActivity() {
    /**
     * 布局中设置的绑定的id
     */
    protected open val variableId: Int = -1 //佈局内的id设置null代表不需要dataBind
    
    /**
     * viewBind的对象
     */
    protected lateinit var viewBind: T
    /**
     * viewModel对象
     */
    protected open  val viewModel :VM by lazy { conversionViewModel() }

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
    /**
     * 初始化绑定viewDataBind
     */
    private fun initViewDataBinding() {
        viewModel.setLoadDialogObserve(this, {
            if (it == null) return@setLoadDialogObserve
            if (it) showDialogLoad() else dismissDialogLoad()
        })
        (viewBind as? ViewDataBinding)?.run {
            if (variableId != -1) setVariable(variableId, viewModel)
            lifecycleOwner = this@BaseActivity
        }
    }


    @CallSuper
    open fun init(savedInstanceState: Bundle?) {
        initView()
        initEvent()
        initModelObserve()
	    viewModel.initModel()
        initData()
    }
    /**
     * 初始化绑定model中的LiveData
     */
    open fun initModelObserve() = Unit
    /**
     * 初始化获取intent传递的数据
     */
    open fun initIntent() = Unit
    /**
     * 初始化点击事件
     */
    open fun initEvent() = Unit
    /**
     * 初始化View
     */
    open fun initView() = Unit
    /**
     * 初始化数据
     */
    open fun initData() = Unit
    /**
     * 设置标题栏文字颜色 白色和黑色
     */
    @Suppress("DEPRECATION")
    private fun stateBarTextColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && whiteStateBarText) window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    /**
     * 是否是白色标题栏
     */
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