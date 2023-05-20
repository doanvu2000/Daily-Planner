package com.dd.company.dailyplanner.utils

import java.util.*

enum class DayWeek(val index: Int, val value: String, val fullValue: String) {
    Sunday(Calendar.SUNDAY, "CN", "Sunday"),
    Monday(Calendar.MONDAY, "Th 2", "Monday"),
    Tuesday(Calendar.TUESDAY, "Th 3", "Tuesday"),
    Wednesday(Calendar.WEDNESDAY, "Th 4", "Wednesday"),
    Thursday(Calendar.THURSDAY, "Th 5", "Thursday"),
    Friday(Calendar.FRIDAY, "Th 6", "Friday"),
    Saturday(Calendar.SATURDAY, "Th 7", "Saturday");

    companion object {
        fun getDayByFullValue(value: String) = values().find { it.fullValue == value } ?: Sunday
        fun getDayByIndex(index: Int) = values().find { it.index == index } ?: Sunday
        fun getTitleWeek() = values().map { it.value }
    }
}