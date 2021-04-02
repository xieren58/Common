package com.says.common.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.*
import java.util.*

/**
 *  Create by rain
 *  Date: 2020/5/7
 */
object ImageUtil {
    @JvmStatic
    fun exifToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    /**
     * 读取图片的旋转的角度
     */
    @JvmStatic
    fun getBitmapDegree(url: String): Int {
        var degree = 0
        try {
            if (url.isEmpty()) return degree
            // 从指定路径下读取图片，并获取其EXIF信息
            val exifInterface = ExifInterface(url)
            // 获取图片的旋转信息
            val attributeInt = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (attributeInt) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    // Returns the degrees in clockwise. Values are 0, 90, 180, or 270.
    @JvmStatic
    fun getOrientation(jpeg: ByteArray?): Int {
        if (jpeg == null) {
            return 0
        }
        var offset = 0
        var length = 0

        // ISO/IEC 10918-1:1993(E)
        while (offset + 3 < jpeg.size && (jpeg[offset++].toInt() and 0xFF) == 0xFF) {
            val marker: Int = jpeg[offset].toInt() and 0xFF
            if (marker == 0xFF) continue
            offset++
            if (marker == 0xD8 || marker == 0x01) continue
            if (marker == 0xD9 || marker == 0xDA) break

            length = pack(jpeg, offset, 2, false)
            if (length < 2 || offset + length > jpeg.size) {
                return 0
            }
            if (marker == 0xE1 && length >= 8 && pack(
                    jpeg,
                    offset + 2,
                    4,
                    false
                ) == 0x45786966 && pack(jpeg, offset + 6, 2, false) == 0
            ) {
                offset += 8
                length -= 8
                break
            }
            offset += length
            length = 0
        }
        if (length > 8) {
            var tag = pack(jpeg, offset, 4, false)
            if (tag != 0x49492A00 && tag != 0x4D4D002A) return 0
            val littleEndian = tag == 0x49492A00
            var count = pack(jpeg, offset + 4, 4, littleEndian) + 2
            if (count < 10 || count > length) return 0
            offset += count
            length -= count
            count = pack(jpeg, offset - 2, 2, littleEndian)
            while (count-- > 0 && length >= 12) {
                tag = pack(jpeg, offset, 2, littleEndian)
                if (tag == 0x0112) {
                    // We do not really care about type and count, do we?
                    return when (pack(jpeg, offset + 8, 2, littleEndian)) {
                        1 -> 0
                        3 -> 180
                        6 -> 90
                        8 -> 270
                        else -> 0
                    }
                }
                offset += 12
                length -= 12
            }
        }
        return 0
    }

    @JvmStatic
    private fun pack(bytes: ByteArray, offsets: Int, lengths: Int, littleEndian: Boolean): Int {
        var offset = offsets
        var length = lengths
        var step = 1
        if (littleEndian) {
            offset += length - 1
            step = -1
        }
        var value = 0
        while (length-- > 0) {
            value = value shl 8 or (bytes[offset].toInt() and 0xFF)
            offset += step
        }
        return value
    }

    @JvmStatic
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight
                && halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * 保存bitmap
     */
    @JvmStatic
    fun saveBitmap(b: Bitmap, filePath: String): String {
        val jpegName = filePath + File.separator + UUID.randomUUID().toString() + ".jpg"
        return try {
            val font = FileOutputStream(jpegName)
            val bos = BufferedOutputStream(font)
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
            jpegName
        } catch (e: IOException) {
            ""
        }
    }
    private fun getFilePath(context: Context): String {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + File.separator + UUID.randomUUID()
            .toString() + ".jpg"
    }

    fun saveBitmap(context: Context, bt: Bitmap): String {
        val filePath = getFilePath(context)
        Log.d("saveFileTag", "filePath:$filePath")
        val file = File(filePath)
        val parentFile = file.parentFile
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs()
        }
        Log.d("saveFileTag", "parentFile:$parentFile")
        var out: FileOutputStream? = null
        try {
            val rotateBitmap = rotateBitmap(bt)
            out = FileOutputStream(file)
            rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            rotateBitmap.recycle()
        } catch (e: FileNotFoundException) {
            Log.d("saveFileTag", "e:$e")
            e.printStackTrace()
        }
        if (out != null) {
            try {
                out.flush()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return filePath
    }

    private fun rotateBitmap(origin: Bitmap): Bitmap {
        val width = origin.width
        val height = origin.height
        val matrix = Matrix()
        matrix.setRotate(-90F)
        // 围绕原地进行旋转
        val newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
        if (newBM == origin) {
            return newBM
        }
        origin.recycle()
        return newBM
    }
}