package com.rain.baselib.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.rain.baselib.R
import com.rain.baselib.viewModel.BaseViewModel

/**
 *  * base基类 - T为[ViewBinding]。可输入[ViewDataBinding]，[ViewDataBinding]情况下绑定[BaseViewModel]。
 * VM为[BaseViewModel] 根据泛型类型，自动获取viewModel对象，并在[variableId]不为-1的情况下进行[ViewDataBinding]和[BaseViewModel]的绑定
 * 在子类不需要viewModel时，泛型传入[BaseViewModel]即可
 * 根据viewBind自动设置布局
 */
abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {
	/**
	 * 布局中设置的绑定的id
	 */
	protected open val variableId: Int = -1
	/**
	 * viewBind的对象
	 */
	protected abstract val viewModel: BaseViewModel?
	/**
	 * viewModel对象
	 */
	protected lateinit var viewBind: VB

	/**
	 * 布局id
	 */
	@get:LayoutRes
	protected abstract val layoutResId: Int

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		initIntent(savedInstanceState)
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		viewBind = DataBindingUtil.inflate(inflater,layoutResId,container,false)
		return viewBind.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initViewDataBinding()
		initModelObserve()
		init()
	}
	/**
	 * 初始化绑定viewDataBind
	 */
	private fun initViewDataBinding() {
		viewModel?.setLoadDialogObserve(this, {
			if (it == null) return@setLoadDialogObserve
			if (it) showDialogLoad() else dismissDialogLoad()
		})
		viewBind.run {
			if (variableId != -1 && viewModel!=null) setVariable(variableId, viewModel)
			lifecycleOwner = this@BaseFragment
		}
	}
	
	/**
	 * 初始化绑定model中的LiveData
	 */
	open fun initModelObserve() = Unit
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