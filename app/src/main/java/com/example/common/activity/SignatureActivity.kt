package com.example.common.activity

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.common.R
import com.example.common.databinding.ActivitySignatureBinding
import com.rain.baselib.activity.BaseDataBindActivity
import com.rain.baselib.common.singleClick
import com.rain.baselib.viewModel.BaseViewModel
import com.says.common.ui.ImageUtil
import kotlinx.coroutines.launch

class SignatureActivity : BaseDataBindActivity<ActivitySignatureBinding>() {
    override val layoutResId = R.layout.activity_signature
    override val viewModel: BaseViewModel? = null
    override fun initEvent() {
        super.initEvent()
        viewBind.btnClear.singleClick {
            viewBind.signature.clear()
        }
        viewBind.btnSave.singleClick {
            saveBitmap()
        }
        viewBind.btnDate.singleClick {
            viewBind.signature.setIsShowTextAdd(!viewBind.signature.getIsShowTextAdd())
        }
    }

    private fun saveBitmap() {
        lifecycleScope.launch {
            val signatureBitmap = viewBind.signature.getSignatureBitmap()
            if (signatureBitmap == null) {
                Log.d("signatureBitmapTag", "signatureBitmap:$signatureBitmap")
                return@launch
            }
            ImageUtil.saveBitmap(this@SignatureActivity, signatureBitmap)
        }
    }
}