package com.example.common.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.*
import com.example.common.BR
import com.example.common.R
import com.example.common.databinding.ActivityExoVideoBinding
import com.example.common.model.ExoVideoViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import com.rain.baselib.activity.BaseDataBindActivity
import com.rain.baselib.common.singleClick

@Route(path = "/common/exo")
class ExoVideoActivity : BaseDataBindActivity<ActivityExoVideoBinding>() {
	override val layoutResId = R.layout.activity_exo_video
	override val viewModel by viewModels<ExoVideoViewModel>()
	override val variableId  = BR.exoVideoViewModelId
	
	@Autowired(name = "uri")
	lateinit var url :String
	
	override fun initView() {
		setBarColor(R.color.black)
		viewBind.playerView.setShowNextButton(false)
		viewBind.playerView.setShowPreviousButton(false)
	}
	override fun initModelObserve() {
		val build = SimpleExoPlayer.Builder(this).build()
		viewBind.playerView.player = build
		viewModel.setHolderAddUrl(url, build)
		lifecycle.removeObserver(viewModel)
		lifecycle.addObserver(viewModel)
	}
	
	override fun initEvent() {
		super.initEvent()
		viewBind.btnClose.singleClick{finish()}
	}
	
	override val whiteStateBarText = false
	
	override fun onDestroy() {
		viewBind.playerView.player = null
		super.onDestroy()
		
	}
}