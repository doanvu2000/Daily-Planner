package com.dd.company.dailyplanner.data.api

import com.google.gson.annotations.SerializedName

data class AccountBody(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("fullName")
    val fullName: String = "test"
)