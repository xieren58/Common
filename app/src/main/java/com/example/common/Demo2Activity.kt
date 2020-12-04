package com.example.common

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.common.databinding.ActivityDemoBinding
import com.example.common.viewModel.Demo2ViewModel
import com.rain.baselib.activity.BaseActivity
import com.rain.baselib.adapter.BasePagerFgAdapter
import com.says.common.ui.singleClick
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
class Demo2Activity : BaseActivity<ActivityDemoBinding,Demo2ViewModel>() {
    override val variableId = BR.demo2Id

    override fun initView() {
        super.initView()
        initIndicator()
        initPager()
    }

    override fun initModelObserve() {
        super.initModelObserve()
        viewModel.mTitleDataList.observe(this, {
            viewBind.magicIndicator.navigator.notifyDataSetChanged()
        })
        viewModel.mPgDataList.observe(this, {
            Log.d("indexTag", "it:${it.isNullOrEmpty()}")
            fgAdapter?.setData(it)
            Log.d("indexTag", "it1:${it.isNullOrEmpty()}")
            viewBind.pgHome.offscreenPageLimit = it.size
        })
    }

    private var fgAdapter: BasePagerFgAdapter? = null

    private fun initPager() {
        fgAdapter = BasePagerFgAdapter(this)
        viewBind.pgHome.adapter =fgAdapter
        viewBind.pgHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewBind.magicIndicator.onPageSelected(position)
            }
        })
    }

    private fun initIndicator() {
        viewBind.magicIndicator.navigator = CommonNavigator(this).apply {
            adapter = object : CommonNavigatorAdapter() {
                override fun getCount(): Int {
                    return viewModel.mTitleDataList.value?.size ?: 0
                }

                override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                    return ColorTransitionPagerTitleView(context).apply {
                        normalColor = ContextCompat.getColor(this@Demo2Activity, R.color.black_33)
                        selectedColor = ContextCompat.getColor(this@Demo2Activity, R.color.teal_200)
                        text = viewModel.mTitleDataList.value?.get(index) ?: ""
                        singleClick {
                            viewBind.pgHome.currentItem = index
                        }
                    }
                }

                override fun getIndicator(context: Context?): IPagerIndicator {
                    return LinePagerIndicator(context).apply {
                        mode = LinePagerIndicator.MODE_WRAP_CONTENT
                    }
                }
            }
        }
        viewBind.magicIndicator.navigator.notifyDataSetChanged()
    }
}