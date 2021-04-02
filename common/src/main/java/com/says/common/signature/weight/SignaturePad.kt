package com.says.common.signature.weight

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.view.ViewCompat
import com.says.common.R
import com.says.common.signature.builder.Bezier
import com.says.common.signature.builder.SvgBuilder
import com.says.common.signature.listener.OnSignedListener
import com.says.common.signature.listener.ViewTreeObserverCompat
import com.says.common.signature.point.ControlTimedPoints
import com.says.common.signature.point.TimedPoint
import com.says.common.ui.UICommon
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 *  Create by rain
 *  Date: 2021/4/2
 */
class SignaturePad @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attr, defStyleAttr) {
    companion object {
        private const val DEFAULT_ATTR_PEN_MIN_WIDTH_PX = 3
        private const val DEFAULT_ATTR_PEN_MAX_WIDTH_PX = 7
        private const val DEFAULT_ATTR_PEN_COLOR = Color.BLACK
        private const val DEFAULT_ATTR_VELOCITY_FILTER_WEIGHT = 0.9f
        private const val DEFAULT_ATTR_CLEAR_ON_DOUBLE_CLICK = false
        private const val DEFAULT_TEXT_TIPS_SIZE: Float = 40F
    }

    private val mPoints: MutableList<TimedPoint> = mutableListOf() //View state
    private var mIsEmpty: Boolean = false
    private var mHasEditState: Boolean? = null
    private var mLastTouchX: Float = 0F
    private var mLastTouchY: Float = 0F
    private var mLastVelocity = 0f
    private var mLastWidth = 0f

    private var mDirtyRect: RectF? = null
    private var mBitmapSavedState: Bitmap? = null
    private val mSvgBuilder: SvgBuilder = SvgBuilder()

    // Cache
    private val mPointsCache: MutableList<TimedPoint> = mutableListOf()
    private val mControlTimedPointsCached = ControlTimedPoints()
    private val mBezierCached = Bezier()

    //Configurable parameters
    private var mMinWidth = 0
    private var mMaxWidth = 0
    private var mVelocityFilterWeight = 0f
    private var mOnSignedListener: OnSignedListener? = null
    private var mClearOnDoubleClick = false

    //Double click detector
    private val mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            return onDoubleClick()
        }
    })

    //Default attribute values
    private var mSignatureBitmap: Bitmap? = null
    private var mSignatureBitmapCanvas: Canvas? = null

    /**
     * bitmap画笔
     */
    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
    }


    /**
     * 文字画笔
     */
    private val textPaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    /**
     * 是否显示文字
     */
    private var isShowText = false

    init {
        initAttr(attr)
    }

    @SuppressLint("Recycle", "CustomViewStyleable")
    private fun initAttr(attr: AttributeSet?) {
        if (attr == null) return
        val styled = context.obtainStyledAttributes(attr, R.styleable.Signature_Common, 0, 0)
        mMinWidth = styled.getDimensionPixelSize(
            R.styleable.Signature_Common_penMinWidth,
            convertDpToPx(DEFAULT_ATTR_PEN_MIN_WIDTH_PX.toFloat())
        )
        mMaxWidth = styled.getDimensionPixelSize(
            R.styleable.Signature_Common_penMaxWidth,
            convertDpToPx(DEFAULT_ATTR_PEN_MAX_WIDTH_PX.toFloat())
        )
        mPaint.color =
            styled.getColor(R.styleable.Signature_Common_penColor, DEFAULT_ATTR_PEN_COLOR)
        mVelocityFilterWeight = styled.getFloat(
            R.styleable.Signature_Common_velocityFilterWeight,
            DEFAULT_ATTR_VELOCITY_FILTER_WEIGHT
        )
        mClearOnDoubleClick = styled.getBoolean(
            R.styleable.Signature_Common_clearOnDoubleClick,
            DEFAULT_ATTR_CLEAR_ON_DOUBLE_CLICK
        )
        textPaint.color =
            styled.getColor(R.styleable.Signature_Common_textAddSize, DEFAULT_ATTR_PEN_COLOR)
        textPaint.textSize = styled.getDimensionPixelSize(
            R.styleable.Signature_Common_textAddSize,
            UICommon.sp2px(context, DEFAULT_TEXT_TIPS_SIZE)
        ).toFloat()
        isShowText = styled.getBoolean(R.styleable.Signature_Common_isShowTextAdd, false)
        styled.recycle()

        //Dirty rectangle to update only the changed portion of the view
        mDirtyRect = RectF()

        post {
            ensureSignatureBitmap()
            invalidate()
        }
    }

    fun setIsShowTextAdd(isShowText: Boolean) {
        this.isShowText = isShowText
        clear()
        invalidate()
    }

    fun getIsShowTextAdd() = isShowText

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        if (mHasEditState == null || mHasEditState!!) {
            mBitmapSavedState = this.getTransparentSignatureBitmap()
        }
        bundle.putParcelable("signatureBitmap", mBitmapSavedState)
        return bundle
    }

    override fun onRestoreInstanceState(oldState: Parcelable?) {
        var state = oldState
        if (state is Bundle) {
            val bundle = state
            this.setSignatureBitmap(bundle.getParcelable<Parcelable>("signatureBitmap") as? Bitmap)
            mBitmapSavedState = bundle.getParcelable("signatureBitmap")
            state = bundle.getParcelable("superState")
        }
        mHasEditState = false
        super.onRestoreInstanceState(state)
    }

    fun setPenColor(color: Int) {
        mPaint.color = color
    }

    private fun clearView() {
        mSvgBuilder.clear()
        mPoints.clear()
        mLastVelocity = 0f
        mLastWidth = ((mMinWidth + mMaxWidth) / 2).toFloat()
        if (mSignatureBitmap != null) {
            mSignatureBitmap = null
            ensureSignatureBitmap()
        }
        setIsEmpty(true)
        invalidate()
    }

    fun clear() {
        clearView()
        mHasEditState = true
    }

    @Suppress("DEPRECATION")
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false
        val eventX = event.x
        val eventY = event.y
        kotlin.run {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    parent.requestDisallowInterceptTouchEvent(true)
                    mPoints.clear()
                    if (mGestureDetector.onTouchEvent(event)) return@run
                    mLastTouchX = eventX
                    mLastTouchY = eventY
                    addPoint(getNewPoint(eventX, eventY))
                    if (mOnSignedListener != null) mOnSignedListener!!.onStartSigning()
                    resetDirtyRect(eventX, eventY)
                    addPoint(getNewPoint(eventX, eventY))
                    setIsEmpty(false)
                }
                MotionEvent.ACTION_MOVE -> {
                    resetDirtyRect(eventX, eventY)
                    addPoint(getNewPoint(eventX, eventY))
                    setIsEmpty(false)
                }
                MotionEvent.ACTION_UP -> {
                    resetDirtyRect(eventX, eventY)
                    addPoint(getNewPoint(eventX, eventY))
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                else -> return false
            }
        }
        mDirtyRect?.run {
            invalidate(
                (left - mMaxWidth).toInt(),
                (top - mMaxWidth).toInt(),
                (right + mMaxWidth).toInt(),
                (bottom + mMaxWidth).toInt()
            )
        }
        return true
    }

    private fun drawAddText(canvas: Canvas, text: String?, x: Float, y: Float) {
        if (text.isNullOrEmpty()) return
        canvas.rotate(90F, x, y)
        canvas.drawText(text, x, y, textPaint)
        canvas.rotate(-90F, x, y)
    }

    override fun onDraw(canvas: Canvas) {
        mSignatureBitmap?.run {
            canvas.drawBitmap(this, 0f, 0f, mPaint)
        }
    }

    fun setOnSignedListener(listener: OnSignedListener) {
        mOnSignedListener = listener
    }

    fun getSignatureBitmap(): Bitmap? {
        val originalBitmap = getTransparentSignatureBitmap() ?: return null
        val whiteBgBitmap = Bitmap.createBitmap(
            originalBitmap.width,
            originalBitmap.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(whiteBgBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(originalBitmap, 0f, 0f, null)
        return whiteBgBitmap
    }

    fun setSignatureBitmap(signature: Bitmap?) {
        if (signature == null) return
        // View was laid out...
        if (ViewCompat.isLaidOut(this)) {
            clearView()
            ensureSignatureBitmap()
            val tempSrc = RectF()
            val tempDst = RectF()
            val dWidth = signature.width
            val dHeight = signature.height
            val vWidth = width
            val vHeight = height

            // Generate the required transform.
            tempSrc[0f, 0f, dWidth.toFloat()] = dHeight.toFloat()
            tempDst[0f, 0f, vWidth.toFloat()] = vHeight.toFloat()
            val drawMatrix = Matrix()
            drawMatrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.CENTER)
            val canvas = Canvas(mSignatureBitmap!!)
            canvas.drawBitmap(signature, drawMatrix, null)
            setIsEmpty(false)
            invalidate()
        } else {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // Remove layout listener...
                    ViewTreeObserverCompat.removeOnGlobalLayoutListener(viewTreeObserver, this)
                    // Signature bitmap...
                    setSignatureBitmap(signature)
                }
            })
        }
    }

    private fun getTransparentSignatureBitmap(): Bitmap? {
        ensureSignatureBitmap()
        return mSignatureBitmap
    }

    private fun onDoubleClick(): Boolean {
        if (mClearOnDoubleClick) {
            clearView()
            return true
        }
        return false
    }

    private fun getNewPoint(x: Float, y: Float): TimedPoint {
        val mCacheSize = mPointsCache.size
        val timedPoint: TimedPoint = if (mCacheSize == 0) {
            TimedPoint()
        } else mPointsCache.removeAt(mCacheSize - 1)
        return timedPoint.set(x, y)
    }

    private fun recyclePoint(point: TimedPoint) {
        mPointsCache.add(point)
    }

    private fun addPoint(newPoint: TimedPoint) {
        mPoints.add(newPoint)
        val pointsCount = mPoints.size
        if (pointsCount > 3) {
            var tmp: ControlTimedPoints =
                calculateCurveControlPoints(mPoints[0], mPoints[1], mPoints[2])
            val c2 = tmp.c2
            recyclePoint(tmp.c1!!)
            tmp = calculateCurveControlPoints(mPoints[1], mPoints[2], mPoints[3])
            val c3 = tmp.c1
            recyclePoint(tmp.c2!!)
            val curve = mBezierCached.set(mPoints[1], c2, c3, mPoints[2])
            val startPoint = curve.startPoint
            val endPoint = curve.endPoint
            var velocity = endPoint!!.velocityFrom(startPoint!!)
            velocity = if (java.lang.Float.isNaN(velocity)) 0.0f else velocity
            velocity = (mVelocityFilterWeight * velocity
                    + (1 - mVelocityFilterWeight) * mLastVelocity)

            // The new width is a function of the velocity. Higher velocities
            // correspond to thinner strokes.
            val newWidth: Float = strokeWidth(velocity)

            // The Bezier's width starts out as last curve's final width, and
            // gradually changes to the stroke width just calculated. The new
            // width calculation is based on the velocity between the Bezier's
            // start and end mPoints.
            addBezier(curve, mLastWidth, newWidth)
            mLastVelocity = velocity
            mLastWidth = newWidth

            // Remove the first element from the list,
            // so that we always have no more than 4 mPoints in mPoints array.
            recyclePoint(mPoints.removeAt(0))
            recyclePoint(c2!!)
            recyclePoint(c3!!)
        } else if (pointsCount == 1) {
            // To reduce the initial lag make it work with 3 mPoints
            // by duplicating the first point
            val firstPoint = mPoints[0]
            mPoints.add(getNewPoint(firstPoint.x, firstPoint.y))
        }
        mHasEditState = true
    }

    private fun addBezier(curve: Bezier, startWidth: Float, endWidth: Float) {
        mSvgBuilder.append(curve, (startWidth + endWidth) / 2)
        ensureSignatureBitmap()
        val originalWidth = mPaint.strokeWidth
        val widthDelta = endWidth - startWidth
        val drawSteps = ceil(curve.length().toDouble()).toFloat()
        var i = 0
        while (i < drawSteps) {
            val t = i.toFloat() / drawSteps
            val tt = t * t
            val ttt = tt * t
            val u = 1 - t
            val uu = u * u
            val uuu = uu * u
            var x = uuu * curve.startPoint!!.x
            x += 3 * uu * t * curve.control1!!.x
            x += 3 * u * tt * curve.control2!!.x
            x += ttt * curve.endPoint!!.x
            var y = uuu * curve.startPoint!!.y
            y += 3 * uu * t * curve.control1!!.y
            y += 3 * u * tt * curve.control2!!.y
            y += ttt * curve.endPoint!!.y

            // Set the incremental stroke width and draw.
            mPaint.strokeWidth = startWidth + ttt * widthDelta
            mSignatureBitmapCanvas!!.drawPoint(x, y, mPaint)
            expandDirtyRect(x, y)
            i++
        }
        mPaint.strokeWidth = originalWidth
    }

    private fun calculateCurveControlPoints(
        s1: TimedPoint,
        s2: TimedPoint,
        s3: TimedPoint
    ): ControlTimedPoints {
        val dx1 = s1.x - s2.x
        val dy1 = s1.y - s2.y
        val dx2 = s2.x - s3.x
        val dy2 = s2.y - s3.y
        val m1X = (s1.x + s2.x) / 2.0f
        val m1Y = (s1.y + s2.y) / 2.0f
        val m2X = (s2.x + s3.x) / 2.0f
        val m2Y = (s2.y + s3.y) / 2.0f
        val l1 = sqrt((dx1 * dx1 + dy1 * dy1).toDouble()).toFloat()
        val l2 = sqrt((dx2 * dx2 + dy2 * dy2).toDouble()).toFloat()
        val dxm = m1X - m2X
        val dym = m1Y - m2Y
        var k = l2 / (l1 + l2)
        if (java.lang.Float.isNaN(k)) k = 0.0f
        val cmX = m2X + dxm * k
        val cmY = m2Y + dym * k
        val tx = s2.x - cmX
        val ty = s2.y - cmY
        return mControlTimedPointsCached.set(
            getNewPoint(m1X + tx, m1Y + ty),
            getNewPoint(m2X + tx, m2Y + ty)
        )
    }

    private fun strokeWidth(velocity: Float): Float {
        return (mMaxWidth / (velocity + 1)).coerceAtLeast(mMinWidth.toFloat())
    }

    private fun expandDirtyRect(historicalX: Float, historicalY: Float) {
        if (historicalX < mDirtyRect?.left ?: 0F) {
            mDirtyRect?.left = historicalX
        } else if (historicalX > mDirtyRect?.right ?: 0F) {
            mDirtyRect?.right = historicalX
        }
        if (historicalY < mDirtyRect?.top ?: 0F) {
            mDirtyRect?.top = historicalY
        } else if (historicalY > mDirtyRect?.bottom ?: 0F) {
            mDirtyRect?.bottom = historicalY
        }
    }

    private fun resetDirtyRect(eventX: Float, eventY: Float) {
        mDirtyRect?.left = mLastTouchX.coerceAtMost(eventX)
        mDirtyRect?.right = mLastTouchX.coerceAtLeast(eventX)
        mDirtyRect?.top = mLastTouchY.coerceAtMost(eventY)
        mDirtyRect?.bottom = mLastTouchY.coerceAtLeast(eventY)
    }

    private fun setIsEmpty(newValue: Boolean) {
        mIsEmpty = newValue
        if (mIsEmpty) mOnSignedListener?.onClear() else mOnSignedListener?.onSigned()
    }

    private fun ensureSignatureBitmap() {
        Log.d("ensureSignatureTag", "mSignatureBitmap:$mSignatureBitmap")
        var backgroundBitmap = mSignatureBitmap
        if (backgroundBitmap == null) {
            backgroundBitmap = Bitmap.createBitmap(
                width, height,
                Bitmap.Config.ARGB_8888
            )
            mSignatureBitmap = backgroundBitmap
            mSignatureBitmapCanvas = Canvas(backgroundBitmap).apply {
                if (isShowText) {
                    val width = ((backgroundBitmap.width * 3) / 5).toFloat()
                    val backGroundHeight = backgroundBitmap.height
                    drawAddText(this, "年", width, (backGroundHeight - 1200).toFloat())
                    drawAddText(this, "月", width, (backGroundHeight - 900).toFloat())
                    drawAddText(this, "日", width, (backGroundHeight - 600).toFloat())
                }
            }
        }
    }

    private fun convertDpToPx(dp: Float): Int {
        return (context.resources.displayMetrics.density * dp).roundToInt()
    }


}