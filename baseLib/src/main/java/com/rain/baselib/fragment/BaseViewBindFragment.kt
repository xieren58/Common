package com.rain.baselib.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.rain.baselib.R
import com.rain.baselib.common.conversionViewBind
import com.rain.baselib.viewModel.BaseViewModel

/**
 * base基类 默认需要[ViewBinding]
 * [BaseViewModel]为viewModel类，不需要viewModel则传入[null]
 */
abstract class BaseViewBindFragment<VB:ViewBinding> :Fragment() {
	/**
	 * viewBind的对象
	 */
	protected abstract val viewModel: BaseViewModel?
	/**
	 * viewModel对象
	 */
	protected lateinit var viewBind: VB
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		initIntent(savedInstanceState)
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		viewBind = initContentView(inflater,container)
		return viewBind.root
	}
	
	/**
	 * 初始化布局
	 */
	open fun initContentView(inflater: LayoutInflater, container: ViewGroup?) = conversionViewBind<VB>(inflater,container)
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initViewDataBinding()
		init()
	}
	/**
	 * 初始化绑定viewDataBind
	 */
	@CallSuper
	protected open fun initViewDataBinding() {
		viewModel?.setLoadDialogObserve(this, {
			if (it == null) return@setLoadDialogObserve
			if (it) showDialogLoad() else dismissDialogLoad()
		})
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
	
	@CallSuper
	open fun init() {
		initView()
		initEvent()
		viewModel?.initModel()
		initData()
	}
	
	
	private var loading: AlertDialog? = null
	
	/**
	 * 打开loading
	 */
	@SuppressLint("InflateParams")
	fun showDialogLoad() {
		try {
			if (loading == null) loading = AlertDialog.Builder(requireContext()).create()
			val window = loading?.window
			window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
			loading?.setCanceledOnTouchOutside(false)
			if (!loading!!.isShowing) {
				loading?.show()
				loading?.setContentView(
						LayoutInflater.from(requireContext()).inflate(R.layout.layout_loading_dialog, null)
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