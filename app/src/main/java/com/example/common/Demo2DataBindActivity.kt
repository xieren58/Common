package com.example.common

import android.graphics.Color
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common.databinding.ActivityDemoBinding
import com.example.common.viewModel.Demo2ViewModel
import com.google.android.material.tabs.TabLayout
import com.rain.baselib.activity.BaseDataBindActivity
import com.rain.baselib.adapter.BasePagerFgAdapter

/**
 *  Create by rain
 *  Date: 2020/11/2
 */
@Route(path = "/common/list")
class Demo2DataBindActivity : BaseDataBindActivity<ActivityDemoBinding>() {
    override val viewModel by viewModels<Demo2ViewModel>()
    override val variableId = BR.demo2Id
    
    override fun initView() {
        super.initView()
        initPager()
    }
    
    override fun initModelObserve() {
        super.initModelObserve()
        viewModel.mTitleDataList.observe(this, {
            it.forEach { str ->
                val tab = viewBind.tabDemo.newTab().setText(str)
                tab.view.setBackgroundColor(Color.YELLOW)
                viewBind.tabDemo.addTab(tab)
            }
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
        viewBind.pgHome.adapter = fgAdapter
        viewBind.pgHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewBind.tabDemo.selectTab(viewBind.tabDemo.getTabAt(position))
            }
        })
        viewBind.tabDemo.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.view.setBackgroundColor(Color.LTGRAY)
                viewBind.pgHome.currentItem = tab.position
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.view.setBackgroundColor(Color.YELLOW)
            }
            
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    };
    
    override val layoutResId = R.layout.activity_demo
    
}