package com.benyq.compose.open.eye.tools

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


object DateTool {

    fun formatVideoDuration(date: Long, pattern: String = "HH:mm:ss"): String {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.PRC)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
        val dateStr = simpleDateFormat.format(date)

        if (dateStr.startsWith("00:")) {
            return dateStr.substring(3)
        }
        return dateStr
    }
}