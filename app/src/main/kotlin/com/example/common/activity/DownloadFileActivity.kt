package com.example.common.activity

import androidx.activity.viewModels
import com.example.common.BR
import com.example.common.R
import com.example.common.databinding.ActivityDownloadFileBinding
import com.example.common.viewModel.DownloadFileViewModel
import com.rain.baselib.activity.BaseDataBindActivity

class DownloadFileActivity : BaseDataBindActivity<ActivityDownloadFileBinding>() {
    override val layoutResId = R.layout.activity_download_file
    override val viewModel by viewModels<DownloadFileViewModel>()
    override val variableId= BR.downloadFileViewModelId
}