package com.dd.company.dailyplanner.data.api

import com.dd.company.dailyplanner.data.PlanEntity
import retrofit2.Call
import retrofit2.http.*

interface PlanService {
    @GET("api/v1/plans")
    fun getPlan(@Query("email") email: String): Call<PlanResponse>

    @POST("api/v1/plans")
    fun syncPlan(@Query("email") email: String, @Body planBody: List<PlanEntity>): Call<Any>
}