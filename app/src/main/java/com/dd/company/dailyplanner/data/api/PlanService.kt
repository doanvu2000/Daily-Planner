package com.dd.company.dailyplanner.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlanService {
    @GET("api/v1/plans")
    fun getPlan(@Query("email") email: String): Call<PlanResponse>
}