package com.says.common.signature.builder

import com.says.common.signature.point.TimedPoint
import kotlin.math.sqrt

/**
 *  Create by rain
 *  Date: 2021/4/2
 */
class Bezier {
    var startPoint: TimedPoint? = null
    var control1: TimedPoint? = null
    var control2: TimedPoint? = null
    var endPoint: TimedPoint? = null

    fun set(
        startPoint: TimedPoint?,
        control1: TimedPoint?,
        control2: TimedPoint?,
        endPoint: TimedPoint?
    ): Bezier {
        this.startPoint = startPoint
        this.control1 = control1
        this.control2 = control2
        this.endPoint = endPoint
        return this
    }

    fun length(): Float {
        val steps = 10
        var length = 0f
        var cx: Double
        var cy: Double
        var px = 0.0
        var py = 0.0
        var xDiff: Double
        var yDiff: Double
        for (i in 0..steps) {
            val t = i.toFloat() / steps
            cx = point(
                t,
                startPoint?.x ?: 0F,
                control1?.x ?: 0F,
                control2?.x ?: 0F,
                endPoint?.x ?: 0F
            )
            cy = point(
                t,
                startPoint?.y ?: 0F,
                control1?.y ?: 0F,
                control2?.y ?: 0F,
                endPoint?.y ?: 0F
            )
            if (i > 0) {
                xDiff = cx - px
                yDiff = cy - py
                length += sqrt(xDiff * xDiff + yDiff * yDiff).toFloat()
            }
            px = cx
            py = cy
        }
        return length
    }

    fun point(t: Float, start: Float, c1: Float, c2: Float, end: Float): Double {
        return start * (1.0 - t) * (1.0 - t) * (1.0 - t) + 3.0 * c1 * (1.0 - t) * (1.0 - t) * t + 3.0 * c2 * (1.0 - t) * t * t + end * t * t * t
    }
}