package com.example.common.weight

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.example.common.R
import com.example.common.databinding.LayoutEditCodeViewBinding
import com.example.common.listener.VerificationAction
import java.lang.Exception

/**
 *  Create by rain
 *  Date: 2021/3/22
 */
class EditCodeDialog(context: Context) : Dialog(context, R.style.inputDialog) {
	private val viewBind by lazy { LayoutEditCodeViewBinding.inflate(LayoutInflater.from(context)) }
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(viewBind.root)
		initView()
	}
	
	private var listener: ((code: String) -> Unit)? = null
	fun addCodeResultListener(listener: ((String) -> Unit)) {
		this.listener = listener
	}
	fun clearInput(){
		viewBind.amEt.text?.clear()
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
	
	override fun show() {
		super.show()
		val layoutParams = window?.attributes
		val wlp = window?.attributes
		wlp?.gravity = Gravity.BOTTOM
		wlp?.width = WindowManager.LayoutParams.MATCH_PARENT
		wlp?.height = WindowManager.LayoutParams.WRAP_CONTENT
		window?.attributes = wlp
		
		layoutParams?.windowAnimations = R.style.take_photo_anim
		layoutParams?.gravity = Gravity.BOTTOM
		layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
		layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
		window?.decorView?.setPadding(0, 0, 0, 0)
		window?.attributes = layoutParams
	}
	
	/**
	 * 关闭输入法
	 */
	private fun closeMethod() {
		viewBind.amEt.isFocusable = false
		viewBind.amEt.isFocusableInTouchMode = false
		try {
			val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			imm.hideSoftInputFromWindow(viewBind.amEt.windowToken, 0)
		} catch (e: Exception) {
		}
	}
	
}