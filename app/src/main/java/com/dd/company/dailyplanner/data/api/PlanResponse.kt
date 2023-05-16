package com.dd.company.dailyplanner.data.api

import com.dd.company.dailyplanner.data.PlanEntity
import com.google.gson.annotations.SerializedName

class PlanResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: List<PlanEntity>
)