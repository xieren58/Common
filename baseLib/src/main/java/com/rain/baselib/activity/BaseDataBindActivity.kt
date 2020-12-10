package com.rain.baselib.activity

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rain.baselib.viewModel.BaseViewModel

/**
 * dataBind基类 - VB 为[ViewDataBinding]
 * 在[variableId]不为-1的情况下进行[ViewDataBinding]和[BaseViewModel]的绑定
 */
abstract class BaseDataBindActivity<VB : ViewDataBinding> : BaseViewBindActivity<VB>() {
	/**
	 * 布局中设置的绑定的id
	 */
	protected open val variableId: Int = -1 //佈局内的id设置null代表不需要dataBind
	
	/**
	 * 布局id
	 */
	@get:LayoutRes
	protected abstract val layoutResId: Int
	/**
	 * 初始化绑定model中的LiveData
	 */
	open fun initModelObserve() = Unit
	
	override fun initContentView(): VB =DataBindingUtil.setContentView(this, layoutResId)
	
	override fun initViewDataBinding() {
		super.initViewDataBinding()
		viewBind.run {
			if (variableId != -1 && viewModel != null) setVariable(variableId, viewModel)
			lifecycleOwner = this@BaseDataBindActivity
		}
		initModelObserve()
	}
	
}