package com.says.common.file.utils

import android.content.Context
//import android.os.Looper
//import io.microshow.rxffmpeg.RxFFmpegCommandList
//import io.microshow.rxffmpeg.RxFFmpegInvoke
import java.io.File


/**
 *  Create by rain
 *  Date: 2020/9/15
 */
object CompressUtils {
	/**
	 * 同步压缩文件
	 */
	fun compressFile(context: Context, originalPath: String): File? {
//		return Luban.with(context).ignoreBy(2048).setCompress(100).get(originalPath)
		return File(originalPath)
	}
	
//	fun compressVideo(originalPath: String): File? {
//		val outUrl = Utils.getFileVideoCompressPath() + File.separator + originalPath.getRandomFileName()
////		"ffmpeg -y -i /storage/emulated/0/1/input.mp4 -b 2097k -r 30 -vcodec libx264 -preset superfast /storage/emulated/0/1/result.mp4".split(" ").toTypedArray()
//		RxFFmpegInvoke.getInstance().runCommand(RxFFmpegCommandList()
//                .append("-i")
//                .append(originalPath)
//                .append("-b")
//                .append("2097k")
//                .append("-r")
//                .append("50")
//                .append("-vcodec")
//                .append("libx264")
//                .append("-preset")
//                .append("superfast")
//                .append(outUrl).build(), null)
//
//		return File(outUrl)
//	}
//
//	fun clearCompress(){
//		try {
//			RxFFmpegInvoke.getInstance().exit()
//		} catch (e: Exception) {
//			e.printStackTrace()
//		}
//	}
}