package com.rain.baselib.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rain.baselib.viewModel.BaseViewModel

/**
 * dataBind基类 - VB 为[ViewDataBinding]
 * 在[variableId]不为-1的情况下进行[ViewDataBinding]和[BaseViewModel]的绑定
 */
abstract class BaseDataBindFragment<VB : ViewDataBinding> : BaseViewBindFragment<VB>() {
	/**
	 * 布局中设置的绑定的id
	 */
	protected open val variableId: Int = -1

	/**
	 * 布局id
	 */
	@get:LayoutRes
	protected abstract val layoutResId: Int
	
	override fun initContentView(inflater: LayoutInflater, container: ViewGroup?): VB {
		return DataBindingUtil.inflate(inflater,layoutResId,container,false)
	}
	/**
	 * 初始化绑定viewDataBind
	 */
	override fun initViewDataBinding() {
		super.initViewDataBinding()
		viewBind.run {
			if (variableId != -1 && viewModel!=null) setVariable(variableId, viewModel)
			lifecycleOwner = this@BaseDataBindFragment
		}
		initModelObserve()
	}
	
	/**
	 * 初始化绑定model中的LiveData
	 */
	open fun initModelObserve() = Unit
}