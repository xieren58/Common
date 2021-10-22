package com.says.common.minType

/**
 *  Create by rain
 *  Date: 2020/10/28
 */
object MimTypeConstants {
    val mimArrayTypes = arrayOf(
        arrayOf(".3gp", "video/3gpp"),
        arrayOf(".apk", "application/vnd.android.package-archive"),
        arrayOf(".asf", "video/x-ms-asf"),
        arrayOf(".avi", "video/x-msvideo"),
        arrayOf(".bin", "application/octet-stream"),
        arrayOf(".bmp", "image/bmp"),
        arrayOf(".c", "text/plain"),
        arrayOf(".class", "application/octet-stream"),
        arrayOf(".conf", "text/plain"),
        arrayOf(".cpp", "text/plain"),
        arrayOf(".doc", "application/msword"),
        arrayOf(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        arrayOf(".xls", "application/vnd.ms-excel"),
        arrayOf(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        arrayOf(".exe", "application/octet-stream"),
        arrayOf(".gif", "image/gif"),
        arrayOf(".gtar", "application/x-gtar"),
        arrayOf(".gz", "application/x-gzip"),
        arrayOf(".h", "text/plain"),
        arrayOf(".htm", "text/html"),
        arrayOf(".html", "text/html"),
        arrayOf(".jar", "application/java-archive"),
        arrayOf(".java", "text/plain"),
        arrayOf(".jpeg", "image/jpeg"),
        arrayOf(".jpg", "image/jpeg"),
        arrayOf(".js", "application/x-javascript"),
        arrayOf(".log", "text/plain"),
        arrayOf(".m3u", "audio/x-mpegurl"),
        arrayOf(".m4a", "audio/mp4a-latm"),
        arrayOf(".m4b", "audio/mp4a-latm"),
        arrayOf(".m4p", "audio/mp4a-latm"),
        arrayOf(".m4u", "video/vnd.mpegurl"),
        arrayOf(".m4v", "video/x-m4v"),
        arrayOf(".mov", "video/quicktime"),
        arrayOf(".mp2", "audio/x-mpeg"),
        arrayOf(".mp3", "audio/x-mpeg"),
        arrayOf(".mp4", "video/mp4"),
        arrayOf(".mpc", "application/vnd.mpohun.certificate"),
        arrayOf(".mpe", "video/mpeg"),
        arrayOf(".mpeg", "video/mpeg"),
        arrayOf(".mpg", "video/mpeg"),
        arrayOf(".mpg4", "video/mp4"),
        arrayOf(".mpga", "audio/mpeg"),
        arrayOf(".msg", "application/vnd.ms-outlook"),
        arrayOf(".ogg", "audio/ogg"),
        arrayOf(".pdf", "application/pdf"),
        arrayOf(".png", "image/png"),
        arrayOf(".pps", "application/vnd.ms-powerpoint"),
        arrayOf(".ppt", "application/vnd.ms-powerpoint"),
        arrayOf(
            ".pptx",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
        ),
        arrayOf(".prop", "text/plain"),
        arrayOf(".rc", "text/plain"),
        arrayOf(".rmvb", "audio/x-pn-realaudio"),
        arrayOf(".rtf", "application/rtf"),
        arrayOf(".sh", "text/plain"),
        arrayOf(".tar", "application/x-tar"),
        arrayOf(".tgz", "application/x-compressed"),
        arrayOf(".txt", "text/plain"),
        arrayOf(".wav", "audio/x-wav"),
        arrayOf(".wma", "audio/x-ms-wma"),
        arrayOf(".wmv", "audio/x-ms-wmv"),
        arrayOf(".wps", "application/vnd.ms-works"),
        arrayOf(".xml", "text/plain"),
        arrayOf(".z", "application/x-compress"),
        arrayOf(".zip", "application/x-zip-compressed"),
        arrayOf("", "*/*")
    )


    val fileArrayTypes = arrayOf(
        arrayOf(".3gp", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".apk", MIMECommon.FILE_TYPE_APK),
        arrayOf(".asf", MIMECommon.FILE_TYPE_VIDEO),
        arrayOf(".avi", MIMECommon.FILE_TYPE_VIDEO),
        arrayOf(".bin", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".bmp", MIMECommon.FILE_TYPE_IMAGE),
        arrayOf(".c", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".class", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".conf", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".cpp", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".doc", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".docx", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".xls", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".xlsx", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".exe", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".gif", MIMECommon.FILE_TYPE_IMAGE),
        arrayOf(".gtar", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".gz", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".h", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".htm", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".html", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".jar", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".java", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".jpeg", MIMECommon.FILE_TYPE_IMAGE),
        arrayOf(".jpg", MIMECommon.FILE_TYPE_IMAGE),
        arrayOf(".js", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".log", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".m3u", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".m4a", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".m4b", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".m4p", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".m4u", MIMECommon.FILE_TYPE_VIDEO),
        arrayOf(".m4v", MIMECommon.FILE_TYPE_VIDEO),
        arrayOf(".mov", MIMECommon.FILE_TYPE_VIDEO),
        arrayOf(".mp2", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".mp3", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".mp4", MIMECommon.FILE_TYPE_VIDEO),
        arrayOf(".mpc", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".mpe", MIMECommon.FILE_TYPE_VIDEO),
        arrayOf(".mpeg", MIMECommon.FILE_TYPE_VIDEO),
        arrayOf(".mpg", MIMECommon.FILE_TYPE_VIDEO),
        arrayOf(".mpg4", MIMECommon.FILE_TYPE_VIDEO),
        arrayOf(".mpga", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".msg", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".ogg", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".pdf", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".png", MIMECommon.FILE_TYPE_IMAGE),
        arrayOf(".pps", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".ppt", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".pptx", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".prop", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".rc", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".rmvb", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".rtf", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".sh", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".tar", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".tgz", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".txt", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".wav", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".wma", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".wmv", MIMECommon.FILE_TYPE_AUDIO),
        arrayOf(".wps", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".xml", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".z", MIMECommon.FILE_TYPE_OTHER),
        arrayOf(".zip", MIMECommon.FILE_TYPE_OTHER),
        arrayOf("", MIMECommon.FILE_TYPE_OTHER)
    )
}