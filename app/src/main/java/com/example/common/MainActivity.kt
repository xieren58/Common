package com.example.common

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.common.activity.SignatureActivity
import com.example.common.databinding.ActivityMainBinding
import com.example.common.model.CityModel
import com.example.common.viewModel.MainViewModel
import com.example.common.weight.EditCodeDialog
import com.rain.baselib.activity.BaseDataBindActivity
import com.rain.baselib.common.createRegisterNoIntentActivity
import com.rain.baselib.common.singleClick
import com.rain.baselib.common.startAc

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
//            startAc<AddressFlutterActivity>()
//            viewModel.cityModel.name = "修改"
            startAc<DemoActivity>()
//            viewModel.testBreak()
//            startAc<StartNavigationActivity>("loginOut" to true)
//            finish()
//			showScanDialog()
        }
        viewBind.tvEnd.singleClick {
//            viewModel.testLaunchNoLine()
			startAc<Demo2DataBindActivity>()
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
}