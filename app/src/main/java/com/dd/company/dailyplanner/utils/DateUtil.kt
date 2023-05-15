package com.dd.company.dailyplanner.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    const val DATE_FORMAT_HOUR_MINUTES = "yyyy-MM-dd-hh-mm"
    private val calendar: Calendar = Calendar.getInstance()
    fun getMonthInt() = calendar.get(Calendar.MONTH)
    fun getYear() = calendar.get(Calendar.YEAR)

    @SuppressLint("SimpleDateFormat")
    fun getTimeFormat(time: Long, pattern: String): String {
        val date = Date(time)
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateToLong(date: String, pattern: String): Long {
        val df = SimpleDateFormat(pattern)
        return df.parse(date)?.time ?: 0
    }

    fun diffTime(startTime: Long, endTime: Long): String {
        val timeFormat = getTimeFormat(endTime - startTime, DATE_FORMAT_HOUR_MINUTES)
        val split = timeFormat.split("-")
        val years = split[0].toIntOrNull()
        val months = split[1].toIntOrNull()
        val days = split[2].toIntOrNull()
        val hours = split[3].toIntOrNull()
        val minutes = split[4].toIntOrNull()
        var rs = ""
        years?.let {
            if (it > 0) {
                rs += "$it năm "
            }
        }
        months?.let {
            if (it > 0) {
                rs += "$it tháng "
            }
        }
        days?.let {
            if (it > 0) {
                rs += "$it ngày "
            }
        }
        hours?.let {
            if (it > 0) {
                rs += "$it giờ "
            }
        }
        minutes?.let {
            if (it > 0) {
                rs += "$it phút "
            }
        }
        return rs
    }

}