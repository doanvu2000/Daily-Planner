package com.dd.company.dailyplanner.data.api

import com.google.gson.annotations.SerializedName

data class AccountResponse(
    @SerializedName("status")
    val status: Boolean
)
