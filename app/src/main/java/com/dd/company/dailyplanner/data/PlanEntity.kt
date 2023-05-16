package com.dd.company.dailyplanner.data

import com.google.gson.annotations.SerializedName

data class PlanEntity(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("icon")
    var icon: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("content")
    var content: String = "",
    @SerializedName("startTime")
    var startTime: Long = 0L,
    @SerializedName("endTime")
    var endTime: Long = 0L,
    @SerializedName("type")
    var type: Int = 0,
    @SerializedName("userId")
    var userId: Long = 0L,
    @SerializedName("isDone")
    var isDone: Boolean = false
)