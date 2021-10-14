package com.example.common.weight

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.common.R
import com.example.common.databinding.LayoutEditCodeViewBinding
import com.example.common.listener.VerificationAction
import java.lang.Exception
import android.widget.RelativeLayout

import android.view.Gravity

import android.view.WindowManager


/**
 *  Create by rain
 *  Date: 2021/8/30
 */
class EditCodeFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.fragment_dialog);
    }
    
    private lateinit var viewBind: LayoutEditCodeViewBinding
    
    private var listener: ((code: String) -> Unit)? = null
    fun addCodeResultListener(listener: ((String) -> Unit)) {
        this.listener = listener
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return DataBindingUtil.inflate<LayoutEditCodeViewBinding>(inflater, R.layout.layout_edit_code_view, container, false).apply {
            viewBind = this
        }.root
        
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    
    fun clearInput() {
        viewBind.amEt.text?.clear()
    }

//	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//		val create = AlertDialog.Builder(requireContext()).create()
//		create.setView(	DataBindingUtil.inflate<LayoutEditCodeViewBinding>(LayoutInflater.from(context), R.layout.layout_edit_code_view,view as? ViewGroup,false).apply {
//			viewBind = this
//		}.root)
//		initView()
//		return create
//	}
//
    
    override fun onStart() {
        super.onStart()
        val layoutParams = dialog?.window?.attributes
        val wlp = dialog?.window?.attributes
        wlp?.gravity = Gravity.BOTTOM
        wlp?.width = ViewGroup.LayoutParams.MATCH_PARENT
        wlp?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = wlp
        
        layoutParams?.windowAnimations = R.style.take_photo_anim
        layoutParams?.gravity = Gravity.BOTTOM
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.decorView?.setPadding(0, 0, 0, 0)
        dialog?.window?.attributes = layoutParams
    }
    
    private fun initView() {
        viewBind.amEt.setFigures(4)
        viewBind.amEt.setOnVerificationCodeChangedListener(object : VerificationAction.OnVerificationCodeChangedListener {
            override fun onVerCodeChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("scanEditTag", "onVerCodeChanged-s:$s")
            }
            
            override fun onInputCompleted(s: CharSequence) {
                Log.d("scanEditTag", "onInputCompleted-s:$s")
                listener?.invoke(s.toString())
            }
        })
    }
    
    override fun dismiss() {
        closeMethod()
        super.dismiss()
    }
    
    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        initShow()
    }
    
    private fun initShow() {
    }
    
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        
    }
    
    /**
     * 关闭输入法
     */
    private fun closeMethod() {
        viewBind.amEt.isFocusable = false
        viewBind.amEt.isFocusableInTouchMode = false
        try {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(viewBind.amEt.windowToken, 0)
        } catch (e: Exception) {
        }
    }
    
}