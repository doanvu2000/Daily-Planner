package com.dd.company.dailyplanner.data

data class WeekEntity(
    val title: String,
    val day: Int,
    val month: Int,
    val year: Int,
    var icon: String,
    var haveMission: Boolean = false,
    var isSelected: Boolean = false
)