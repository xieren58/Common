package com.says.common.utils

/**
 *  Create by rain
 *  Date: 2021/9/1
 */
object StringUtils {
    /**
     * 替换字符串占位符方法
     * StringUtils.formatString("我已阅读并且同意%{var}、%{var}、%{var}", "1", "2", "3")
     * @param target
     * @param src
     * @return
     */
    @JvmStatic
    fun formatString(target: String, vararg src: String): String? {
        val pattern = "%\\{var\\}"
        val targetArrays = target.split(pattern).toTypedArray()
        if (targetArrays.size != src.size) {
            return ""
        }
        val builder = StringBuilder()
        for (i in targetArrays.indices) {
            builder.append(targetArrays[i] + src[i])
        }
        return builder.toString()
    }

    /**
     * 字符串拼接
     * @param strArray 多个字符串集合
     */
    @JvmStatic
    fun strSplicing(vararg strArray: String): String {
        if (strArray.isEmpty()) return ""
        val builder = StringBuilder()
        strArray.forEach { builder.append(it) }
        return builder.toString()
    }

}