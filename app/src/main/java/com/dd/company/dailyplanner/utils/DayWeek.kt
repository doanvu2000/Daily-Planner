package com.dd.company.dailyplanner.utils

import java.util.*

enum class DayWeek(val index: Int, val value: String) {
    Sunday(Calendar.SUNDAY, "CN"),
    Monday(Calendar.MONDAY, "Th 2"),
    Tuesday(Calendar.TUESDAY, "Th 3"),
    Wednesday(Calendar.WEDNESDAY, "Th 4"),
    Thursday(Calendar.THURSDAY, "Th 5"),
    Friday(Calendar.FRIDAY, "Th 6"),
    Saturday(Calendar.SATURDAY, "Th 7");

    companion object {
        fun getDayByIndex(index: Int) = values().find { it.index == index } ?: Sunday
        fun getTitleWeek() = values().map { it.value }
    }
}