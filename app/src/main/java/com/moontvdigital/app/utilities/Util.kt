package com.moontvdigital.app.utilities

import android.content.Context
import com.google.gson.Gson

class Util {

    companion object {

        const val BASE_URL = "https://film.ofpa.in/getdetails.asmx/"

        @JvmStatic
        fun dpToPx(ctx: Context, dp: Int): Int {
            val density = ctx.resources.displayMetrics.density
            return Math.round(dp.toFloat() * density)
        }

        @JvmStatic
        fun formatVideoDuration(videoDurationStr: String): String {
            if (videoDurationStr.isEmpty()) {
                return ""
            }
            val splitArray = videoDurationStr.split(":")
            val hourStr = splitArray[0]
            val minStr = splitArray[1]
            val secStr = splitArray[2]
            if (hourStr.toInt() == 0) {
                return minStr + "min "
            }
            return hourStr + "m " + minStr + "min "
        }

        fun getGson() : Gson {
            return Gson()
        }
    }

}