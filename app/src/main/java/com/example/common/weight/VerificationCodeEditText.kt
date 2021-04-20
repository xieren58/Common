package com.example.common.weight

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Build.VERSION_CODES
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.common.listener.VerificationAction
import com.example.common.R
import java.util.*

/**
 * 验证码的EditText
 * Created by yj on 2017/6/12.
 */

class VerificationCodeEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatEditText(context, attrs, defStyleAttr), VerificationAction, TextWatcher {
	
	private var mFigures: Int = 0//需要输入的位数
	private var mVerCodeMargin: Int = 0//验证码之间的间距
	private var mBottomSelectedColor: Int = 0//底部选中的颜色
	private var mBottomNormalColor: Int = 0//未选中的颜色
	private var mBottomLineHeight: Float = 0.toFloat()//底线的高度
	private var mSelectedBackgroundColor: Int = 0//选中的背景颜色
	private var mCursorWidth: Int = 0//光标宽度
	private var mCursorColor: Int = 0//光标颜色
	private var mCursorDuration: Int = 0//光标闪烁间隔
	
	private var onCodeChangedListener: VerificationAction.OnVerificationCodeChangedListener? = null
	private var mCurrentPosition = 0
	private var mEachRectLength = 0//每个矩形的边长
	private var isShowBottomLine = true
	private val mSelectedBackgroundPaint by lazy {
		Paint().apply {
			strokeWidth = dp2px(1F)
			color = Color.parseColor("#00AEBD")
			style = Paint.Style.STROKE
		}
	}
	private val mNormalBackgroundPaint by lazy {
		Paint().apply {
			strokeWidth = dp2px(1F)
			style = Paint.Style.STROKE
			color = Color.parseColor("#DDE1E2")
		}
	}
	private val mBottomSelectedPaint by lazy { Paint() }
	private val mBottomNormalPaint by lazy { Paint() }
	private val mCursorPaint by lazy { Paint() }
	
	// 控制光标闪烁
	private var isCursorShowing: Boolean = false
	private var mCursorTimerTask: TimerTask? = null
	private var mCursorTimer: Timer? = null
	
	init {
		initAttrs(attrs)
		setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))//防止出现下划线
		initPaint()
		initCursorTimer()
		isFocusableInTouchMode = true
		super.addTextChangedListener(this)
	}
	
	/**
	 * 初始化paint
	 */
	private fun initPaint() {
		mBottomSelectedPaint.color = mBottomSelectedColor
		mBottomNormalPaint.color = mBottomNormalColor
		mBottomSelectedPaint.strokeWidth = mBottomLineHeight
		mBottomNormalPaint.strokeWidth = mBottomLineHeight
		
		mCursorPaint.isAntiAlias = true
		mCursorPaint.color = mCursorColor
		mCursorPaint.style = Paint.Style.FILL_AND_STROKE
		mCursorPaint.strokeWidth = mCursorWidth.toFloat()
	}
	
	/**
	 * 初始化Attrs
	 */
	@SuppressLint("ObsoleteSdkInt", "CustomViewStyleable")
	private fun initAttrs(attrs: AttributeSet?) {
		val ta = context.obtainStyledAttributes(attrs, R.styleable.VerCodeEditText)
		mFigures = ta.getInteger(R.styleable.VerCodeEditText_figures, 4)
		mVerCodeMargin = ta.getDimension(R.styleable.VerCodeEditText_verCodeMargin, 0f).toInt()
		mBottomSelectedColor = ta.getColor(R.styleable.VerCodeEditText_bottomLineSelectedColor,
				currentTextColor)
		mBottomNormalColor = ta.getColor(R.styleable.VerCodeEditText_bottomLineNormalColor,
				getColor(android.R.color.darker_gray))
		mBottomLineHeight = ta.getDimension(R.styleable.VerCodeEditText_bottomLineHeight,
				dp2px(5F))
		mSelectedBackgroundColor = ta.getColor(R.styleable.VerCodeEditText_selectedBackgroundColor,
				getColor(android.R.color.darker_gray))
		mCursorWidth = ta.getDimension(R.styleable.VerCodeEditText_cursorWidth, dp2px(1F)).toInt()
		mCursorColor = ta.getColor(R.styleable.VerCodeEditText_cursorColor, getColor(android.R.color.darker_gray))
		mCursorDuration = ta.getInteger(R.styleable.VerCodeEditText_cursorDuration, DEFAULT_CURSOR_DURATION)
		isShowBottomLine = ta.getBoolean(R.styleable.VerCodeEditText_isShowBottomLine, true)
		ta.recycle()
		
		// force LTR because of bug: https://github.com/JustKiddingBaby/VercodeEditText/issues/4
		if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
			layoutDirection = LAYOUT_DIRECTION_LTR
		}
	}
	
	private fun initCursorTimer() {
		mCursorTimerTask = object : TimerTask() {
			override fun run() {
				// 通过光标间歇性显示实现闪烁效果
				isCursorShowing = !isCursorShowing
				postInvalidate()
			}
		}
		mCursorTimer = Timer()
	}
	
	override fun onAttachedToWindow() {
		super.onAttachedToWindow()
		
		// 启动定时任务，定时刷新实现光标闪烁
		mCursorTimer?.scheduleAtFixedRate(mCursorTimerTask, 0, mCursorDuration.toLong())
	}
	
	override fun onDetachedFromWindow() {
		super.onDetachedFromWindow()
		mCursorTimer?.cancel()
	}
	
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		val widthResult: Int
		val heightResult: Int
		//最终的宽度
		val widthMode = MeasureSpec.getMode(widthMeasureSpec)
		val widthSize = MeasureSpec.getSize(widthMeasureSpec)
		widthResult = if (widthMode == MeasureSpec.EXACTLY) {
			widthSize
		} else {
			getScreenWidth(context)
		}
		//每个矩形形的宽度
		mEachRectLength = (widthResult - mVerCodeMargin * (mFigures - 1)) / mFigures
		//最终的高度
		val heightMode = MeasureSpec.getMode(heightMeasureSpec)
		val heightSize = MeasureSpec.getSize(heightMeasureSpec)
		heightResult = if (heightMode == MeasureSpec.EXACTLY) {
			heightSize
		} else {
			mEachRectLength
		}
		setMeasuredDimension(widthResult, heightResult)
	}
	
	@SuppressLint("ClickableViewAccessibility")
	override fun onTouchEvent(event: MotionEvent): Boolean {
		if (event.action == MotionEvent.ACTION_DOWN) {
			requestFocus()
			setSelection(text?.length ?: 0)
			showKeyBoard(context)
			return false
		}
		return super.onTouchEvent(event)
	}
	
	override fun onDraw(canvas: Canvas) {
		mCurrentPosition = text?.length ?: 0
		val strokeWidth = mNormalBackgroundPaint.strokeWidth
		
		val width = mEachRectLength - paddingLeft - paddingRight
		val height = measuredHeight - paddingTop - paddingBottom
		//画矩形边框
		for (i in 0 until mFigures) {
			canvas.save()
			val start = width * i + i * mVerCodeMargin
			val end = width + start
			val patient = if (i <= mCurrentPosition) mSelectedBackgroundPaint else mNormalBackgroundPaint //是否选中
			canvas.drawRect(start.toFloat() + strokeWidth, strokeWidth, end.toFloat() - strokeWidth, height - strokeWidth, patient)
			canvas.restore()
		}
		//绘制文字
		val value = text.toString()
		for (i in value.indices) {
			canvas.save()
			val start = width * i + i * mVerCodeMargin
			val x = (start + width / 2).toFloat()
			val paint = paint
			paint.textAlign = Paint.Align.CENTER
			paint.color = currentTextColor
			val fontMetrics = paint.fontMetrics
			val baseline = (height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top
			canvas.drawText(value[i].toString(), x, baseline, paint)
			canvas.restore()
		}
		if (isShowBottomLine) {
			//绘制底线
			for (i in 0 until mFigures) {
				canvas.save()
				val lineY = height - mBottomLineHeight / 2
				val start = width * i + i * mVerCodeMargin
				val end = width + start
				if (i < mCurrentPosition) {
					canvas.drawLine(start.toFloat(), lineY, end.toFloat(), lineY, mBottomSelectedPaint)
				} else {
					canvas.drawLine(start.toFloat(), lineY, end.toFloat(), lineY, mBottomNormalPaint)
				}
				canvas.restore()
			}
		}
		//绘制光标
		if (!isCursorShowing && isCursorVisible && mCurrentPosition < mFigures && hasFocus()) {
			canvas.save()
			val startX = mCurrentPosition * (width + mVerCodeMargin) + width / 2
			val startY = height / 4
			val endY = height - height / 4
			canvas.drawLine(startX.toFloat(), startY.toFloat(), startX.toFloat(), endY.toFloat(), mCursorPaint)
			canvas.restore()
		}
	}
	
	override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
		val editable = text ?: return
		mCurrentPosition = editable.length
		postInvalidate()
	}
	
	override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
		val editable = text ?: return
		mCurrentPosition = editable.length
		postInvalidate()
		onCodeChangedListener?.onVerCodeChanged(editable, start, before, count)
	}
	
	override fun afterTextChanged(s: Editable) {
		val editable = text ?: return
		mCurrentPosition = editable.length
		postInvalidate()
		if (editable.length == mFigures) {
			onCodeChangedListener?.onInputCompleted(editable)
		} else if (editable.length > mFigures) {
			text?.delete(mFigures, editable.length)
		}
	}
	
	override fun setFigures(figures: Int) {
		mFigures = figures
		postInvalidate()
	}
	
	override fun setVerCodeMargin(margin: Int) {
		mVerCodeMargin = margin
		postInvalidate()
	}
	
	override fun setBottomSelectedColor(@ColorRes bottomSelectedColor: Int) {
		mBottomSelectedColor = getColor(bottomSelectedColor)
		postInvalidate()
	}
	
	override fun setBottomNormalColor(@ColorRes bottomNormalColor: Int) {
		mBottomSelectedColor = getColor(bottomNormalColor)
		postInvalidate()
	}
	
	override fun setSelectedBackgroundColor(@ColorRes selectedBackground: Int) {
		mSelectedBackgroundColor = getColor(selectedBackground)
		postInvalidate()
	}
	
	override fun setBottomLineHeight(bottomLineHeight: Int) {
		this.mBottomLineHeight = bottomLineHeight.toFloat()
		postInvalidate()
	}
	
	override fun setOnVerificationCodeChangedListener(listener: VerificationAction.OnVerificationCodeChangedListener) {
		this.onCodeChangedListener = listener
	}
	
	/**
	 * 返回颜色
	 */
	private fun getColor(@ColorRes color: Int): Int {
		return ContextCompat.getColor(context, color)
	}
	
	/**
	 * dp转px
	 */
	private fun dp2px(dp: Float): Float {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				resources.displayMetrics)
	}
	
	private fun showKeyBoard(context: Context) {
		val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
	}
	
	companion object {
		private const val DEFAULT_CURSOR_DURATION = 400
		
		/**
		 * 获取手机屏幕的宽度
		 */
		@Suppress("DEPRECATION")
		internal fun getScreenWidth(context: Context): Int {
			val metrics = DisplayMetrics()
			val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
			wm.defaultDisplay.getMetrics(metrics)
			return metrics.widthPixels
		}
	}
}
