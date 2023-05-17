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
        val diff: Long = Date(endTime).time - Date(startTime).time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        var rs = ""
        if (days > 0) {
            rs += "$days ngày "
        }
        if (hours > 0) {
            rs += "$hours giờ "
        }
        if (minutes > 0) {
            rs += "$minutes phút "
        }
        return rs
    }

    fun checkToDay(time: Long): Boolean {
        val timeFormat = getTimeFormat(time, DATE_FORMAT_HOUR_MINUTES)
        val split = timeFormat.split("-")
        val years = split[0].toIntOrNull()
        val months = split[1].toIntOrNull()
        val days = split[2].toIntOrNull()
        val calendar = Calendar.getInstance()
        if (days != calendar.get(Calendar.DAY_OF_MONTH)) {
            return false
        }
        if (months != calendar.get(Calendar.MONTH) + 1) {
            return false
        }
        if (years != calendar.get(Calendar.YEAR)) {
            return false
        }
        return true
    }

    fun checkMatchTime(time: Long, day: Int, month: Int, year: Int): Boolean {
        val timeFormat = getTimeFormat(time, DATE_FORMAT_HOUR_MINUTES)
        val split = timeFormat.split("-")
        val years = split[0].toIntOrNull() ?: 0
        val months = split[1].toIntOrNull() ?: 0
        val days = split[2].toIntOrNull() ?: 0
        return days == day && months == month && years == year
    }

    fun formatHourMinutes(hour: Int, minutes: Int): String {
        var rs = ""
        rs += if (hour < 10) {
            "0$hour:"
        } else {
            "$hour:"
        }
        rs += if (minutes < 10) {
            "0$minutes"
        } else {
            "$minutes"
        }
        return rs
    }
}