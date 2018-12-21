package com.ukyoo.v2client.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat

object TimeFormatUtils{

    fun toLong(dateString: String): Long {
        val stringArray = dateString.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var created = System.currentTimeMillis() / 1000
        var how = 0
        try {
            how = Integer.parseInt(stringArray[0])
        } catch (e: Exception) {

        }

        val subString = stringArray[1].substring(0, 1)
        if (subString == "分") {
            created -= (60 * how).toLong()
        } else if (subString == "小") {
            created -= (3600 * how).toLong()
        } else if (subString == "天") {
            created -= (24 * 3600 * how).toLong()
        } else {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val date = sdf.parse(dateString)
                created = date.time / 1000
            } catch (e: Exception) {
            }

        }

        return created
    }

    @JvmStatic
    fun toString(ts: Long): String {
        if (ts == -1L) return ""
        val created = ts * 1000
        val now = System.currentTimeMillis()
        val difference = now - created
        val text = if (difference >= 0 && difference <= DateUtils.MINUTE_IN_MILLIS)
            "刚刚"
        else
            DateUtils.getRelativeTimeSpanString(
                created,
                now,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            )
        return text.toString()
    }
}