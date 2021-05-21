package com.example.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.adapter.PhotoWeightAdapter
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.rain.baselib.activity.BaseRecActivity
import com.rain.baselib.databinding.ActivityBaseRecBinding
import com.says.common.file.enum.PushFromTypeEnum
import com.says.common.file.listener.FilePushResultListener
import com.says.common.file.utils.FilePushManager
import com.says.common.ui.UICommon
import com.says.common.utils.JsonManagerHelper

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
class DemoActivity : BaseRecActivity<ActivityBaseRecBinding>() {
    override val viewModel by viewModels<DemoListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setBarColor(R.color.white)
        super.onCreate(savedInstanceState)
    }

    override fun getRecLayoutManager() = GridLayoutManager(this, 2)
    override val loadRefreshEnable: Boolean
        get() = false
    override val loadMoreEnable: Boolean
        get() = false

    override fun loadRecItemDecoration(): RecyclerView.ItemDecoration {
        return GridSpacingItemDecoration(2, UICommon.dip2px(MyApp.context, 15F), false)
    }

    override fun clickRecItem(position: Int) {
        val cityModel = viewModel.getItemData(position) ?: return
        if (cityModel.itemType == PhotoWeightAdapter.ADD_TYPE) {
            PictureUtils.onPickFromAll(this, isSingle = false, maxSize = 9, blockResultBlock = {
                pushFileData(it)
            })
        } else {
            cityModel.status = 1
            cityModel.process = cityModel.process + 1
            viewModel.adapter.notifyItemChanged(position, "itemRefresh")
        }
    }

    private fun pushFileData(list: MutableList<String>?) {
        if (list.isNullOrEmpty()) return
        val picList: MutableList<UpdatePic> = mutableListOf()
        list.forEach {
            picList.add(UpdatePic().apply {
                this.url = it
                this.process = 0
            })
        }
        viewModel.adapter.addItemData(picList)
//		pushFile()
    }

    private fun pushFile() {
        viewModel.getLists()?.forEach {
            if (it.itemType == PhotoWeightAdapter.ADD_TYPE) return@forEach
            if (it.url.isNullOrEmpty() || it.status == 1 || it.status == 2) return@forEach
            it.status = 1
            it.process = 0
            viewModel.adapter.updateItemData(it)

            FilePushManager.uploadFile(this, it.url, object : FilePushResultListener {
                override fun pushSuccess(path: String) {
                    it.url = path
                    it.status = 2
                    it.process = 100
                    viewModel.adapter.updateItemData(it)
                }

                override fun pushFail() {
                    it.status = 3
                    it.process = 0
                    viewModel.adapter.updateItemData(it)
                }

                override fun pushProgress(progress: Int) {
                    it.status = 1
                    it.process = progress
                    viewModel.adapter.updateItemData(it)
                }
            }, false, PushFromTypeEnum.PUSH_FILE_TO_ALI)
        }
    }

    override fun init() {
        setBarColor(R.color.white)
        super.init()
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
}