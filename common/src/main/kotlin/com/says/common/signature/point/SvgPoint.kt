package com.says.common.signature.point

import kotlin.math.roundToInt

/**
 *  Create by rain
 *  Date: 2021/4/2
 */
class SvgPoint {
    private var x: Int = 0
    private var y: Int = 0

    constructor(point: TimedPoint?) {
        x = point?.x?.roundToInt() ?: 0
        y = point?.y?.roundToInt() ?: 0
    }

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun toAbsoluteCoordinates(): String {
        return StringBuilder().append(x).append(",").append(y).toString()
    }

    fun toRelativeCoordinates(referencePoint: SvgPoint?): String {
        return SvgPoint(x - (referencePoint?.x ?: 0), y - (referencePoint?.y ?: 0)).toString()
    }

    override fun toString(): String {
        return toAbsoluteCoordinates()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val svgPoint = other as SvgPoint
        return if (x != svgPoint.x) false else y == svgPoint.y
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}