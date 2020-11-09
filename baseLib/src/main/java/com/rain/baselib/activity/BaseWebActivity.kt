package com.rain.baselib.activity

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.webkit.*
import androidx.lifecycle.ViewModelProvider
import com.rain.baselib.BR
import com.rain.baselib.R
import com.rain.baselib.common.singleClick
import com.rain.baselib.viewModel.BaseWebViewModel
import kotlinx.android.synthetic.main.activity_base_web.*
import kotlinx.android.synthetic.main.base_title.*
import kotlinx.android.synthetic.main.layout_err_view.*

abstract class BaseWebActivity : BaseActivity() {
	override val layoutResId = R.layout.activity_base_web
	override val viewModel by lazy { ViewModelProvider(this).get(BaseWebViewModel::class.java) }
	override val variableId = BR.base_web_id
	override fun initView() {
		initToolbar()
		initWeb()
	}
	
	//初始化toolbar
	private fun initToolbar() {
		val toolbarTitle = getToolbarTitle()
		if (!toolbarTitle.isNullOrEmpty()) tv_title?.text = toolbarTitle
		toolbar?.setNavigationIcon(R.drawable.arrow_left)
		toolbar?.title = ""
		setSupportActionBar(toolbar)
		toolbar?.setNavigationOnClickListener {
			val canGoBack = web_base?.canGoBack()
			if (canGoBack != null && canGoBack) web_base?.goBack() else finish()
		}
	}
	
	abstract fun getToolbarTitle(): String?//获取title文字
	abstract fun webLoadFinish() //webview加载完毕
	abstract fun getWebUrl(): String?//webview加载链接
	
	@SuppressLint("SetJavaScriptEnabled")
	private fun initWeb() {
		val settings = web_base?.settings
		settings?.javaScriptEnabled = true
		settings?.cacheMode = WebSettings.LOAD_NO_CACHE
		settings?.blockNetworkImage = false//解决图片不显示
		settings?.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
		settings?.javaScriptCanOpenWindowsAutomatically = true
		settings?.domStorageEnabled = true
		web_base?.webViewClient = object : WebViewClient() {
			override fun onPageFinished(view: WebView, url: String) {
				super.onPageFinished(view, url)
				webLoadFinish()
			}
		}
		setWebListener()
		viewModel.loadUrl(getWebUrl())
	}
	
	override fun initEvent() {
		super.initEvent()
		ll_error?.singleClick { viewModel.loadUrl(getWebUrl()) }
	}
	
	
	open fun setWebListener() {
		web_base?.webChromeClient = object : WebChromeClient() {
			override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
				result?.confirm()
				return true
			}
			
			override fun onProgressChanged(view: WebView?, newProgress: Int) {
				super.onProgressChanged(view, newProgress)
				viewModel.setPro(newProgress)
			}
		}
	}
	
	override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			val canGoBack = web_base?.canGoBack()
			if (canGoBack != null && canGoBack) {
				web_base?.goBack()
				return true
			}
		}
		return super.onKeyDown(keyCode, event)
	}
	
	override fun onDestroy() {
		super.onDestroy()
		web_base?.destroy()
	}
}