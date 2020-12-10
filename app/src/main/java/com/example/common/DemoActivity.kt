package com.example.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.rain.baselib.activity.BaseRecActivity
import com.rain.baselib.databinding.ActivityBaseRecBinding
import com.says.common.utils.JsonManagerHelper

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
class DemoActivity : BaseRecActivity<ActivityBaseRecBinding>() {
    override val viewModel by lazy { ViewModelProvider(this).get(DemoListViewModel::class.java) }
    override fun getRecLayoutManager() = GridLayoutManager(this, 4)
    override val loadRefreshEnable: Boolean
        get() = false
    override val loadMoreEnable: Boolean
        get() = false

    override fun clickRecItem(position: Int) {

    }
    
    override val rightStr = "确认"

    override fun rightTvClick() {
        setResult(RESULT_OK, Intent().apply {
            putExtra("demo", JsonManagerHelper.getHelper().objToStr(viewModel.getLists()))
        })
        finish()
    }

    override fun initIntent(savedInstanceState: Bundle?) {
        super.initIntent(savedInstanceState)
        val requestData = intent?.getStringExtra("requestData")
        Log.d("resultTag", "requestData:$requestData")
    }

    override fun initEvent() {
        super.initEvent()
        viewModel.adapter.setPhotoItemClickListener(object : PhotoWeightAdapter.PhotoItemClickListener {
            override fun itemAdd() {
                val lists = viewModel.getPhotoList()
                if (lists.size >= 4) return
                PictureUtils.onPickFromGallery(this@DemoActivity, isSingle = false, maxSize = 4 - lists.size) {
                    if (!it.isNullOrEmpty()) viewModel.addPhoto(it)
                }
            }

            override fun itemClick(position: Int) {
            }

            override fun itemDelete(position: Int) {
                viewModel.adapter.removeItemData(position)
            }
        })
    }
}