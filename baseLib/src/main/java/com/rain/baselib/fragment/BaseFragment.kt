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
import com.rain.baselib.R
import com.rain.baselib.viewModel.BaseViewModel

/**
 *  Create by rain
 *  Date: 2020/11/6
 */
abstract class BaseFragment : Fragment() {
    @get:LayoutRes
    protected abstract val layoutResId: Int

    protected open val variableId: Int = -1
    protected abstract val viewModel: BaseViewModel?

    private var dataBind: ViewDataBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initIntent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBind = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return dataBind!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewDataBinding()
        initModelObserve()
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

    open fun initModelObserve() = Unit
    open fun initView() = Unit
    open fun initData() = Unit
    open fun initIntent() = Unit
    open fun initEvent() = Unit

    @CallSuper
    open fun init(savedInstanceState: Bundle?) {
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