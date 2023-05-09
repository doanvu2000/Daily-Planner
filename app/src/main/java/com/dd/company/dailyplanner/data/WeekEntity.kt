package com.dd.company.dailyplanner.data

data class WeekEntity(
    val title: String,
    val day: Int,
    val icon: String,
    var haveMission: Boolean = false,
    var isSelected: Boolean = false
)