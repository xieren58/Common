package com.rain.baselib.weight

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.rain.baselib.R
import com.rain.baselib.common.singleClick
import com.rain.baselib.databinding.LayoutToolbarBaseTitleBinding

/**
 *  Create by rain
 *  Date: 2021/2/8
 */
class ToolbarLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
	
	private val dataBind by lazy { LayoutToolbarBaseTitleBinding.inflate(LayoutInflater.from(context), this, false) }
	
	init {
		addView(dataBind.root, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
		initAttrs(attrs)
	}
	
	@SuppressLint("Recycle", "CustomViewStyleable")
	private fun initAttrs(attrs: AttributeSet?) {
		val styled = context.obtainStyledAttributes(attrs, R.styleable.toolbar_title_style)
		val titleStr = styled.getString(R.styleable.toolbar_title_style_titleStr)
		val rightStr = styled.getString(R.styleable.toolbar_title_style_rightStr)
		val leftIcon = styled.getResourceId(R.styleable.toolbar_title_style_leftIcon, R.drawable.arrow_left)
		val rightIcon = styled.getResourceId(R.styleable.toolbar_title_style_rightIcon, -1)
		val showViewLine = styled.getBoolean(R.styleable.toolbar_title_style_showViewLine, true)
		val showRightImage = styled.getBoolean(R.styleable.toolbar_title_style_showRightImage, true)
		val showRightTv = styled.getBoolean(R.styleable.toolbar_title_style_showRightTv, true)
		styled.recycle()
		dataBind.baseTitleToolbar.title = ""
		dataBind.baseTitleTvTitle.text = if (!titleStr.isNullOrEmpty()) titleStr else ""
		dataBind.baseTitleIgRight.visibility = if (rightIcon == -1) View.GONE else {
			dataBind.baseTitleIgRight.setImageResource(rightIcon)
			if (showRightImage) View.VISIBLE else View.GONE
		}
		dataBind.baseTitleToolbar.setNavigationIcon(leftIcon)
		dataBind.baseTitleTvRight.visibility = if (rightStr.isNullOrEmpty()) View.GONE else {
			dataBind.baseTitleTvRight.text = rightStr
			if (showRightTv) View.VISIBLE else View.GONE
		}
		dataBind.baseTitleViewLine.visibility = if (showViewLine) View.VISIBLE else View.GONE
	}
	
	/**
	 * 设置左侧按钮点击
	 */
	fun setLeftClickListener(clickListener: OnClickListener) {
		dataBind.baseTitleToolbar.setNavigationOnClickListener(clickListener)
	}
	
	/**
	 * 设置右侧图片点击
	 */
	fun setRightImageClickListener(clickListener: OnClickListener) {
		dataBind.baseTitleIgRight.singleClick(clickListener)
	}
	
	/**
	 * 设置右侧文字点击
	 */
	fun setRightTvClickListener(clickListener: OnClickListener) {
		dataBind.baseTitleTvRight.singleClick(clickListener)
	}
	
	/**
	 * 设置title文字
	 */
	fun setTitleStr(title: String?) {
		dataBind.baseTitleTvTitle.text = title ?: ""
	}
	
	/**
	 * 设置右侧图标
	 */
	fun setRightIcon(@DrawableRes resId: Int) {
		dataBind.baseTitleIgRight.setImageResource(resId)
	}
	
	/**
	 * 设置右侧文字
	 */
	fun setRightStr(str: String?) {
		dataBind.baseTitleTvRight.text = str ?: ""
	}
	
	/**
	 * 设置是否显示下划线
	 */
	fun setShowViewLine(isShow: Boolean) {
		dataBind.baseTitleViewLine.visibility = if (isShow) View.VISIBLE else View.GONE
	}
	
	/**
	 * 设置是否显示右侧图标
	 */
	fun setShowRightImage(isShow: Boolean) {
		dataBind.baseTitleIgRight.visibility = if (isShow) View.VISIBLE else View.GONE
	}
	
	/**
	 * 设置是否显示右侧文字
	 */
	fun setShowRightTv(isShow: Boolean) {
		dataBind.baseTitleTvRight.visibility = if (isShow) View.VISIBLE else View.GONE
	}
	
	/**
	 * 设置左侧图片
	 */
	fun setLeftImage(@DrawableRes resId: Int) {
		dataBind.baseTitleToolbar.setNavigationIcon(resId)
	}
	
	/**
	 * 获取右侧textView
	 */
	fun getRightTv() = dataBind.baseTitleTvRight
	
	/**
	 * 获取右侧imageView
	 */
	fun getRightImage() = dataBind.baseTitleIgRight
	
	/**
	 * 获取title控件
	 */
	fun getTitleTv() = dataBind.baseTitleTvTitle
	
	/**
	 * 获取左侧按钮
	 */
	fun getToolbar() = dataBind.baseTitleToolbar
	
	/**
	 * 初始化toolbar
	 */
	fun initToolbar(activity: AppCompatActivity) {
		activity.setSupportActionBar(dataBind.baseTitleToolbar)
	}
}