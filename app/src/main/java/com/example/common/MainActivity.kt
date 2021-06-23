package com.example.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Environment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.common.activity.ExoVideoActivity
import com.example.common.databinding.ActivityMainBinding
import com.example.common.viewModel.MainViewModel
import com.example.common.weight.EditCodeDialog
import com.rain.baselib.activity.BaseDataBindActivity
import com.rain.baselib.common.singleClick
import com.rain.baselib.common.startAc
import com.says.common.ui.ImageUtil
import com.says.common.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@AndroidEntryPoint
class MainActivity : BaseDataBindActivity<ActivityMainBinding>() {
	override val layoutResId = R.layout.activity_main
	override val viewModel by viewModels<MainViewModel>()
	override val variableId = BR.mainId
	private var accuracy = 3
	private val cameraLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
	
	}
	
	override fun initEvent() {
		super.initEvent()
		viewBind.tvStart.singleClick {
			val checkIgnoreBatteryOptimizations =
				PermissionUtils.checkIgnoreBatteryOptimizations(this)
			Log.d("batteryTag","checkIgnoreBatteryOptimizations:$checkIgnoreBatteryOptimizations")
			if (!checkIgnoreBatteryOptimizations){
				PermissionUtils.requestIgnoreBatteryOptimizations(this)
			}
		}
		viewBind.tvEnd.singleClick {
		val isOpen =	PermissionUtils.openBatterySettings(this)
			Log.d("batteryTag","isOpen:$isOpen")
		}
		viewBind.content.singleClick {
			viewBind.etNumber.clearFocus()
		}
		viewBind.etNumber.setOnFocusChangeListener { _, hasFocus ->
			if (hasFocus) return@setOnFocusChangeListener
			val textContent = viewBind.etNumber.text.toString().trim()
			val keepNumberStr = NumberUtils.getKeepNumberStr(this, textContent, accuracy)
			Log.d("editTag", "keepNumberStr:$keepNumberStr")
			if (!keepNumberStr.isNullOrEmpty()) {
				viewBind.etNumber.setText(keepNumberStr)
			}
		}
		setEndLabelStr(
				view = viewBind.tvTest,
				"呼吸道合胞病毒（RSV）感染呼吸道合胞病毒 感染呼吸道合胞病毒（RSV）感染呼吸道合胞病毒 感染",
				haveCh = true,
				haveEn = true
		)
	}
	
	private suspend fun loadBitmap(path:String = "https://91trial.oss-cn-shanghai.aliyuncs.com/91trial/file_3cd75d4a-859f-46e0-8e3c-2f9b763041dd.jpeg"): Bitmap? {
		return suspendCancellableCoroutine { continuation ->
			continuation.invokeOnCancellation {
				Log.d("bitmapSuspendTag","it:$it")
			}
			Glide.with(this).asBitmap()
					.load(path)
					.into(object : CustomTarget<Bitmap>() {
						override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
							Log.d("bitmapSuspendTag","resource:$resource")
							val map = mutableMapOf<Pair<Float, Float>, Pair<Float, Float>>()
							map[0F to 100F] = 20F to 100F
							map[100F to 300F] = 20F to 100F
							map[200F to 500F] = 20F to 100F
							map[300F to 600F] = 20F to 100F
							continuation.resume(ImageUtil.mosaicBitmap(this@MainActivity, resource, map))
						}
						
						override fun onLoadCleared(placeholder: Drawable?) {
						}
						
						override fun onLoadFailed(errorDrawable: Drawable?) {
							super.onLoadFailed(errorDrawable)
							Log.d("bitmapSuspendTag","errorDrawable:$errorDrawable")
							continuation.resume(null)
//							continuation.resumeWithException(Exception("获取图片失败"))
						}
					})
		}
	}
	
	private fun initMosaic() {
		lifecycleScope.launch {
			val loadBitmap = loadBitmap()?:return@launch
			Log.d("bitmapSuspendTag","loadBitmap:$loadBitmap")
			ImageUtil.saveBitmap(this@MainActivity, loadBitmap)
		}
	}
	
	private var editScanDialog: EditCodeDialog? = null
	private fun showScanDialog() {
		if (editScanDialog == null) editScanDialog = EditCodeDialog(this)
		editScanDialog?.addCodeResultListener {
			Log.d("scanEditTag", "it:$it")
			if (it == "12345") {
				editScanDialog?.clearInput()
			}
		}
		editScanDialog?.setCancelable(true)
		editScanDialog?.setCanceledOnTouchOutside(true)
		editScanDialog?.setOnDismissListener { editScanDialog = null }
		editScanDialog?.show()
	}
	
	private fun setEndLabelStr(view: TextView, str: String?, haveCh: Boolean, haveEn: Boolean) {
		val content = str ?: ""
		val stringBuilder = SpannableStringBuilder(content)
		if (haveCh) appendTextSpan("中", Color.parseColor("#005FA2"), stringBuilder)
		if (haveEn) appendTextSpan("英", Color.parseColor("#FF9F00"), stringBuilder)
		view.text = stringBuilder
		
	}
	
	private fun appendTextSpan(str: String, color: Int, stringBuilder: SpannableStringBuilder) {
		stringBuilder.append(" ")
		stringBuilder.append(str)
		stringBuilder.setSpan(
				RoundedBackgroundSpan(color, str),
				stringBuilder.length - str.length,
				stringBuilder.length,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
		)
	}
	
	private fun runClazzCatch(){
		try {
			val forName = Class.forName("com.example.common.DemoListViewModel")
			val field = forName.getField("Companion").get(null) as DemoListViewModel.Companion
			field.test()
//			val field = forName.getField("INSTANCE").get(null) as NumberUtils
//			val invoke =	field.getKeepNumberStr(this, "18.022", 2)
//			Log.d("catchMethodTag","invoke:$invoke")
//			forName.getDeclaredMethod()
//			forName.newInstance()
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
	
	private fun loadFilePath(){
		lifecycleScope.launch {
			val path =  "${Environment.getExternalStorageDirectory()}/Android/data/${packageName}/files/Pictures/"
			externalCacheDir
			val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
			Log.d("loadFileTag","path:$path")
			Log.d("loadFileTag","externalFilesDir:${externalFilesDir?.path}")
			val file = File(path,"508426e2-d800-45eb-861f-62e7b1aa84ca.jpg")
			Log.d("loadFileTag","file:${file.exists()}")
			val loadBitmap = loadBitmap(file.path)
			Log.d("loadFileTag","loadBitmap:$loadBitmap")
			viewBind.igTest.setImageBitmap(loadBitmap)
		}
		
	}
}