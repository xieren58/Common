package com.says.common.signature.builder

import com.says.common.signature.point.SvgPoint

/**
 *  Create by rain
 *  Date: 2021/4/2
 */
class SvgPathBuilder constructor(private val mStartPoint: SvgPoint, private val mStrokeWidth: Int) {
    companion object {
        const val SVG_RELATIVE_CUBIC_BEZIER_CURVE = 'c'
        const val SVG_MOVE = 'M'
    }

    private val mStringBuilder by lazy { StringBuilder() }
    private var mLastPoint: SvgPoint? = null

    init {
        mLastPoint = mStartPoint
        mStringBuilder.append(SVG_RELATIVE_CUBIC_BEZIER_CURVE)
    }

    fun getStrokeWidth(): Int {
        return mStrokeWidth
    }

    fun getLastPoint(): SvgPoint? {
        return mLastPoint
    }

    fun append(
        controlPoint1: SvgPoint,
        controlPoint2: SvgPoint,
        endPoint: SvgPoint
    ): SvgPathBuilder {
        mStringBuilder.append(makeRelativeCubicBezierCurve(controlPoint1, controlPoint2, endPoint))
        mLastPoint = endPoint
        return this
    }

    private fun makeRelativeCubicBezierCurve(
        controlPoint1: SvgPoint,
        controlPoint2: SvgPoint,
        endPoint: SvgPoint
    ): String {
        val sControlPoint1 = controlPoint1.toRelativeCoordinates(mLastPoint)
        val sControlPoint2 = controlPoint2.toRelativeCoordinates(mLastPoint)
        val sEndPoint = endPoint.toRelativeCoordinates(mLastPoint)
        val sb = java.lang.StringBuilder()
        sb.append(sControlPoint1)
        sb.append(" ")
        sb.append(sControlPoint2)
        sb.append(" ")
        sb.append(sEndPoint)
        sb.append(" ")

        // discard zero curve
        val svg = sb.toString()
        return if ("c0 0 0 0 0 0" == svg) {
            ""
        } else {
            svg
        }
    }

    override fun toString(): String {
        return StringBuilder()
            .append("<path ")
            .append("stroke-width=\"")
            .append(mStrokeWidth)
            .append("\" ")
            .append("d=\"")
            .append(SVG_MOVE)
            .append(mStartPoint)
            .append(mStringBuilder)
            .append("\"/>")
            .toString()
    }
}