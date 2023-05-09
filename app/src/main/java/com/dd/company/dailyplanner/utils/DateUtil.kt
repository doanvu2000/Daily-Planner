package com.dd.company.dailyplanner.utils

import java.util.*

object DateUtil {
    val calendar = Calendar.getInstance()
    fun getMonthInt() = calendar.get(Calendar.MONTH)
    fun getYear() = calendar.get(Calendar.YEAR)
}