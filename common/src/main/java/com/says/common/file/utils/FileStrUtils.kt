package com.says.common.file.utils

import android.util.Log
import okhttp3.internal.and
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.util.*


/**
 *  Create by rain
 *  Date: 2019/12/24
 */


fun String.getRandomFileName(): String {
	return "file_" + UUID.randomUUID().toString() + substring(lastIndexOf("."))
}

fun File.fileToMd5(): String? {
	Log.d("PutObject", "isFile:${isFile}")
	if (!isFile) {
		return null
	}
	val digest: MessageDigest?
	val `in`: FileInputStream?
	val buffer = ByteArray(1024)
	var len: Int
	try {
		digest = MessageDigest.getInstance("MD5")
		`in` = FileInputStream(this)
		while (`in`.read(buffer, 0, 1024).also { len = it } != -1) {
			digest.update(buffer, 0, len)
		}
		`in`.close()
	} catch (e: Exception) {
		Log.d("PutObject", "e:$e")
		return null
	}
	return digest.digest().bytesToHexString()
}

fun ByteArray.bytesToHexString(): String? {
	val stringBuilder = StringBuilder("")
	Log.d("PutObject", "src:$this")
	if (this.isEmpty()) {
		return null
	}
	for (i in this.indices) {
		val v: Int = this[i] and 0xFF
		val hv = Integer.toHexString(v)
		if (hv.length < 2) {
			stringBuilder.append(0)
		}
		stringBuilder.append(hv)
	}
	return stringBuilder.toString()
}