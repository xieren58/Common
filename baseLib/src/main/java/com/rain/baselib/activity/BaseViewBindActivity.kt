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
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.rain.baselib.R
import com.rain.baselib.common.conversionViewBind
import com.rain.baselib.viewModel.BaseViewModel

/**
 * base基类 默认需要[ViewBinding]
 *  [BaseViewModel]为viewModel类，不需要viewModel则传入[null]
 */
abstract class BaseViewBindActivity<VB : ViewBinding> : AppCompatActivity() {
	/**
	 * viewBind的对象
	 */
	protected lateinit var viewBind: VB
	
	/**
	 * viewModel对象
	 */
	protected abstract val viewModel: BaseViewModel?
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		stateBarTextColor()
		viewBind = initContentView()
		ARouter.getInstance().inject(this)
		initViewDataBinding()
		initIntent(savedInstanceState)
		init()
	}
	
	/**
	 * 初始化绑定viewDataBind
	 */
	@CallSuper
	protected open fun initViewDataBinding() {
		viewModel?.setLoadDialogObserve(this) {
			if (it == null) return@setLoadDialogObserve
			if (it) showDialogLoad() else dismissDialogLoad()
		}
	}
	
	/**
	 * 初始化布局
	 */
	open fun initContentView(): VB = conversionViewBind<VB>().apply { setContentView(root) }
	
	@CallSuper
	open fun init() {
		initView()
		initEvent()
		viewModel?.initModel()
		initData()
	}
	
	/**
	 * 初始化获取intent传递的数据
	 */
	open fun initIntent(savedInstanceState: Bundle?) = Unit
	
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
			loading?.setCancelable(false)
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