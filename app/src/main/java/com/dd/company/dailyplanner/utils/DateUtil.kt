package com.dd.company.dailyplanner.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    const val DATE_FORMAT_HOUR_MINUTES = "yyyy-MM-dd-hh-mm"
    private val calendar: Calendar = Calendar.getInstance()
    fun getMonthInt() = calendar.get(Calendar.MONTH)
    fun getDayInt() = calendar.get(Calendar.DAY_OF_MONTH)
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
        val NUM_DAY = 1000 * 60 * 60 * 24
        val NUM_HOUR = 1000 * 60 * 60
        val NUM_MINUTES = 1000 * 60
        val NUM_SECOND = 1000
        val days = diff / NUM_DAY
        val hours = (diff - days * NUM_DAY) / NUM_HOUR
        val minutes = (diff - days * NUM_DAY - hours * NUM_HOUR) / NUM_MINUTES
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

    private val mapDayCountOfMonth = mutableMapOf(
        1 to 31, 3 to 31, 4 to 30, 5 to 31, 6 to 30, 7 to 31, 8 to 31, 9 to 30, 10 to 31, 11 to 30, 12 to 31
    )

    fun getDayCountOfMonth(monthNumber: Int) = mapDayCountOfMonth[monthNumber]

    fun getHourMinuteFormatFromLong(time: Long): String {
        val hour = time.getHour()
        val minutes = time.getMinutes()
        return formatHourMinutes(hour, minutes)
    }

    fun getDateFormatFromLong(time: Long): String {
        val timeFormat = getTimeFormat(time, DATE_FORMAT_HOUR_MINUTES)
        var year = ""
        var month = 0
        var day = 0
        timeFormat.split("-").also {
            year = it[0]
            month = it[1].toInt()
            day = it[2].toInt()
        }
        return String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year
    }

    fun compareTwoTime(hour1: Int, minutes: Int, hour2: Int, minutes2: Int): Boolean {
        if (hour1 > hour2) {
            return false
        }
        if (hour1 < hour2) {
            return true
        }
        if (minutes < minutes2) {
            return true
        }
        if (minutes >= minutes2) {
            return false
        }
        return true
    }

    fun Long.toCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar
    }

    fun Long.getYear(): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar.get(Calendar.YEAR)
    }

    fun Long.getMonth(): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar.get(Calendar.MONTH) + 1
    }

    fun Long.getDay(): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun Long.getHour(): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun Long.getMinutes(): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar.get(Calendar.MINUTE)
    }
}