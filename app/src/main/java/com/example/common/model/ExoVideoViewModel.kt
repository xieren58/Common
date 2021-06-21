package com.example.common.model

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.rain.baselib.viewModel.BaseViewModel

/**
 *  Create by rain
 *  Date: 2021/6/21
 */
class ExoVideoViewModel :BaseViewModel() , AnalyticsListener, LifecycleObserver {
	private var videoUrl: String? = null //视频地址
	private var player: SimpleExoPlayer? = null //默认的视频播放器
	val viewLoadShow = MutableLiveData(true) //是否显示load
	fun setHolderAddUrl(videoUrl: String?, player: SimpleExoPlayer) {
		viewLoadShow.postValue(true)
		this.videoUrl = videoUrl
		this.player = player
		Log.d("videoPlayTag", "videoUrl:$videoUrl,player:$player")
	}
	
	override fun onRenderedFirstFrame(eventTime: AnalyticsListener.EventTime, output: Any, renderTimeMs: Long) {
		super.onRenderedFirstFrame(eventTime, output, renderTimeMs)
		Log.e("videoPlayTag", "onRenderedFirstFrame: 在渲染的第一帧上")
		viewLoadShow.postValue(false)
	}
	
	override fun initModel() {
		player?.addAnalyticsListener(this)
		playVideo()
	}
	
	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
	fun pausePlayer() {
		Log.d("videoPlayTag", "pausePlayer:$player")
		try {
			if (player != null && player!!.isPlaying) {
				player?.pause()
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
	
	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
	fun resumePlayer() {
		Log.d("videoPlayTag", "resumePlayer:$player")
		try {
			if (player != null && !player!!.isPlaying) {
				player?.play()
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
	
	private fun playVideo() {
		val url = videoUrl
		if (url.isNullOrEmpty()) return
		player?.setMediaItem(MediaItem.fromUri(url))
		player?.prepare()
	}
	
	
	override fun onCleared() {
		super.onCleared()
		Log.d("videoPlayTag", "onCleared:$player")
		if (player != null) {
			player?.removeAnalyticsListener(this)
			player?.stop()
			player?.release()
			player = null
		}
		Log.d("videoPlayTag", "onCleared-end:$player")
	}
	
}