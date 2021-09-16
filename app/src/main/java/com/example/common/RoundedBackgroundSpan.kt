package com.example.common

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.style.ReplacementSpan
import android.util.Log
import android.util.TypedValue
import kotlin.properties.Delegates


/**
 *  Create by rain
 *  Date: 2021/1/22
 */
class RoundedBackgroundSpan(private val color: Int, private val str: String) : ReplacementSpan() {
    private var mRightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, MyApp.context.resources.displayMetrics) //右边距
    private var mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, MyApp.context.resources.displayMetrics)
    private var mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, MyApp.context.resources.displayMetrics)
    private var mBgWidth by Delegates.notNull<Float>() //Icon背景宽度
    
    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        Log.d("RoundedBackgroundSpan", "mBgWidth:$mBgWidth,mRightMargin:$mRightMargin")
        return (mBgWidth + mRightMargin).toInt()
    }
    
    init {
        mBgWidth = calculateBgWidth(str)
    }
    
    /**
     * 计算icon背景宽度
     *
     * @param text icon内文字
     */
    private fun calculateBgWidth(text: String): Float {
        //多字，宽度=文字宽度+padding
        val textRect = Rect()
        Paint().apply {
            color = color
            isAntiAlias = true
            textSize = mTextSize
            textAlign = Paint.Align.CENTER
        }.getTextBounds(text, 0, text.length, textRect)
        return textRect.width() + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, MyApp.context.resources.displayMetrics) * 2
    }
    
    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        Log.d("RoundedBackgroundSpan", "draw-mBgWidth:$mBgWidth,mRightMargin:$mRightMargin")
        //画背景
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.isAntiAlias = true
        val metrics = paint.fontMetrics
        
        paint.textSize = mTextSize
        //算出背景开始画的y坐标
        val textHeight = metrics.descent - metrics.ascent
        val bgStartY: Float = y + metrics.ascent
        //画背景
        canvas.drawRoundRect(RectF(x, bgStartY, x + mBgWidth, bgStartY + textHeight), mRadius, mRadius, paint)
        
        //把字画在背景中间
        paint.textSize = mTextSize
        paint.textAlign = Paint.Align.CENTER //这个只针对x有效
        
        canvas.drawText(str, x + mBgWidth / 2, bgStartY - mRightMargin - metrics.top, paint)
    }
}