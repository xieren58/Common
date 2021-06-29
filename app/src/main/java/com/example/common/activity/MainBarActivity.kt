package com.example.common.activity

import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common.R
import com.example.common.databinding.ActivityMainBarBinding
import com.example.common.fragment.MeFragment
import com.example.common.fragment.PatientFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rain.baselib.activity.BaseDataBindActivity
import com.rain.baselib.adapter.BasePagerFgAdapter
import com.rain.baselib.viewModel.BaseViewModel

@Route(path = "/main/testBar")
class MainBarActivity : BaseDataBindActivity<ActivityMainBarBinding>(), BottomNavigationView.OnNavigationItemSelectedListener  {
	override val layoutResId = R.layout.activity_main_bar
	override val viewModel: BaseViewModel?=null
	
	override fun initView() {
		super.initView()
		initBehavior()
		initPager()
		initBottomView()
	}
	private fun initBottomView() {
		viewBind.bottomNavigation.itemIconTintList = null
		val menu = viewBind.bottomNavigation.menu
		menu.add(0, 0, 0, "受试者列表").setIcon(R.drawable.select_tab_main_patient)
		menu.add(0, 1, 1, "我的").setIcon(R.drawable.select_tab_me_patient)
		viewBind.pagerMain.setCurrentItem(0, false)
		viewBind.bottomNavigation.setOnNavigationItemSelectedListener(this)
	}
	private fun initPager() {
		viewBind.pagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				when (position) {
					0 -> viewBind.bottomNavigation.menu.findItem(0)?.isChecked = true
					1 -> viewBind.bottomNavigation.menu.findItem(1)?.isChecked = true
				}
			}
		})
		viewBind.pagerMain.isUserInputEnabled = false
		viewBind.pagerMain.adapter = BasePagerFgAdapter(this).apply {
			setData(mutableListOf(  PatientFragment(),  MeFragment()))
		}
		viewBind.pagerMain.offscreenPageLimit = 2
	}
	
	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			0 -> viewBind.pagerMain.setCurrentItem(0, false)
			1 -> viewBind.pagerMain.setCurrentItem(1, false)
		}
		return false
	}
	private var mBehavior: BottomSheetBehavior<View>? = null
	private fun initBehavior() {
		mBehavior = BottomSheetBehavior.from(viewBind.constraintScroll)
		mBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
			override fun onStateChanged(bottomSheet: View, newState: Int) {
				when (newState) {
					//完全展开状态
					BottomSheetBehavior.STATE_EXPANDED -> {
						Log.d("bottomSheetTag", "STATE_EXPANDED")
					}
					//拖动状态
					BottomSheetBehavior.STATE_DRAGGING -> {
						Log.d("bottomSheetTag", "STATE_DRAGGING")
					}
					//折叠状态
					BottomSheetBehavior.STATE_COLLAPSED -> {
						Log.d("bottomSheetTag", "STATE_COLLAPSED")
					}
					BottomSheetBehavior.STATE_HALF_EXPANDED -> {
						Log.d("bottomSheetTag", "STATE_HALF_EXPANDED")
					}
					BottomSheetBehavior.STATE_HIDDEN -> {
						Log.d("bottomSheetTag", "STATE_HIDDEN")
					}
					BottomSheetBehavior.STATE_SETTLING -> {
						Log.d("bottomSheetTag", "STATE_SETTLING")
					}
				}
			}
			
			override fun onSlide(bottomSheet: View, slideOffset: Float) {
				Log.d("bottomSheetTag", "onSlide:$slideOffset")
				if (slideOffset>0){
					val background = viewBind.constraintScroll.background
					val bgDrawable = ContextCompat.getDrawable(this@MainBarActivity,R.drawable.shape_share_bottom_bg)
					if (background != bgDrawable)viewBind.constraintScroll.background = bgDrawable
				}else{
					val background = viewBind.constraintScroll.background
					val bgDrawable = ContextCompat.getDrawable(this@MainBarActivity,R.drawable.shape_share_bottom_close_bg)
					if (background != bgDrawable)viewBind.constraintScroll.background = bgDrawable
				}
				viewBind.viewBg.alpha = slideOffset
			}
		})
	}
	
}