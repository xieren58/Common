package com.example.common

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import java.math.BigDecimal
import java.text.NumberFormat

/**
 *  Create by rain
 *  Date: 2020/3/30
 */


object NumberUtils {

	fun getKeepNumberStr(context: Context, numberStr: String?, keep: Int): String? {
		Log.d("numberTag", "numberStr:$numberStr")
		Log.d("numberTag", "keep:$keep")
		if (numberStr.isNullOrEmpty()) return null
		try {
			if (keep < 0) return getDoubleFormatStr(context, numberStr)
			return if (numberStr.contains(".")) {
				val split = numberStr.split(".")
				Log.d("numberTag", "split:" + split.size)
				if (split.size >= 2) {
					val s = split[1]
					Log.d("numberTag", "s:" + s.length)
					if (s.length <= keep - 1) {
						return getDoubleFormatStr(context, numberStr, s.length)
					}
				}
				getDoubleFormatStr(context, numberStr, keep)
			} else {
				getFormatStr(context, numberStr)
			}
		} catch (e: Exception) {
			Toast.makeText(context, "格式错误", Toast.LENGTH_SHORT).show()
			Log.d("numberTag", "e:" + e.message)
		}
		return numberStr
	}

	private fun getFormatStr(context: Context, numberStr: String): String? {
		try {
			return NumberFormat.getInstance().apply {
				isGroupingUsed = false
			}.format(numberStr.toInt().toLong())
		} catch (e: Exception) {
			Toast.makeText(context, "格式错误", Toast.LENGTH_SHORT).show()
		}
		return numberStr
	}

	private fun getDoubleFormatStr(context: Context, numberStr: String): String? {
		try {
			return BigDecimal(numberStr).stripTrailingZeros().toPlainString()
		} catch (e: Exception) {
			Toast.makeText(context, "格式错误", Toast.LENGTH_SHORT).show()
		}
		return numberStr
	}

	private fun getDoubleFormatStr(context: Context, number: String, keep: Int): String? {
		var format = number
		try {
			format = NumberFormat.getInstance().apply {
				isGroupingUsed = false
				minimumFractionDigits = keep
			}.format(BigDecimal(number).setScale(keep, BigDecimal.ROUND_HALF_UP).toDouble())
		} catch (e: Exception) {
			Toast.makeText(context, "格式错误", Toast.LENGTH_SHORT).show()
		}
		return format
	}

}