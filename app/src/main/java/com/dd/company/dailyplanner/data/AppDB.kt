package com.dd.company.dailyplanner.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlanEntity::class], version = 1)
abstract class AppDB : RoomDatabase() {

    abstract fun dailyDataDao(): PlanDao
}