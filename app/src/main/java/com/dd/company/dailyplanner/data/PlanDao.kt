package com.dd.company.dailyplanner.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Insert
    suspend fun insertPlan(planEntity: PlanEntity)

    @Update
    suspend fun updatePlan(planEntity: PlanEntity)

    @Delete
    suspend fun deletePlan(planEntity: PlanEntity)

    @Query("SELECT * FROM plans WHERE id = :id")
    fun getPlanById(id: Int): Flow<PlanEntity>

    @Query("SELECT * FROM plans")
    fun getAllPlan(): Flow<List<PlanEntity>>

    @Query("DELETE FROM plans")
    suspend fun deleteAllPlan()

}