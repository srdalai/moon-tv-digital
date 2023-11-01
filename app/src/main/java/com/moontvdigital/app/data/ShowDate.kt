package com.moontvdigital.app.data

import java.text.SimpleDateFormat
import java.util.Date

data class ShowDate(
    val calDate: Date,
    val date: Int,
    val month: Int,
    val year: Int,
    val day: String
) {
    override fun toString(): String {
        return "ShowDate(date='$date', month='$month', year='$year', day='$day')"
    }

    fun getApiCallDate(): String {
        return year.toString() + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", date)
    }

    fun getShowDate(): String {
        val sdf = SimpleDateFormat("dd MMM, E")
        return sdf.format(calDate)
    }
}

