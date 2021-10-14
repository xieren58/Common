package com.example.common

import android.os.Environment
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 *  Create by rain
 *  Date: 2020/7/20
 */
object Logger {
    private const val VERBOSE = 2
    private const val DEBUG = 3
    private const val INFO = 4
    private const val WARN = 5
    private const val ERROR = 6
    private const val ASSERT = 7
    private val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.CHINA)
    private const val SEPARATOR = ","
    private val NEW_LINE = System.getProperty("line.separator")
    
    fun log(priority: Int, tag: String, value: String) {
        val builder = StringBuilder()
        builder.append(NEW_LINE)
        builder.append(dateFormat.format(Date()))
        builder.append(SEPARATOR)
        builder.append(logLevel(priority))
        builder.append(NEW_LINE)
        builder.append("<$tag>")
        builder.append(NEW_LINE)
        builder.append(value)
        // new line
        builder.append(NEW_LINE)
        builder.append(".")
        builder.append(NEW_LINE)
        builder.append(".")
        builder.append(NEW_LINE)
        log(builder.toString())
    }
    
    fun d(tag: String, o: Any?) {
        log(DEBUG, tag, (o ?: "").toString())
    }
    
    fun e(tag: String, o: Any?) {
        log(ERROR, tag, (o ?: "").toString())
    }
    
    fun i(tag: String, o: Any?) {
        log(INFO, tag, (o ?: "").toString())
    }
    
    private fun log(message: String) {
        synchronized(this) {
            val logFile = getLogFile()
            var fileWriter: FileWriter? = null
            try {
                fileWriter = FileWriter(logFile, true)
                fileWriter.write(message)
                fileWriter.flush()
                fileWriter.close()
            } catch (e: IOException) {
                if (fileWriter != null) {
                    try {
                        fileWriter.flush()
                        fileWriter.close()
                    } catch (e1: IOException) { /* fail silently */
                    }
                }
            }
        }
        
    }
    
    
    private fun logLevel(value: Int): String? {
        return when (value) {
            VERBOSE -> "VERBOSE"
            DEBUG -> "DEBUG"
            INFO -> "INFO"
            WARN -> "WARN"
            ERROR -> "ERROR"
            ASSERT -> "ASSERT"
            else -> "UNKNOWN"
        }
    }
    
    fun getLogFile(): File {
        return File(MyApp.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath + File.separatorChar + "logger.txt")
    }


//    fun getFileLastModifiedTime(): Long? {
//        return getLogFile().lastModified()
//    }


}