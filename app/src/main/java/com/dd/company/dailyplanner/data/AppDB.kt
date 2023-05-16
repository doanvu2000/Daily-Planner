package com.dd.company.dailyplanner.data

import androidx.room.RoomDatabase

abstract class AppDB : RoomDatabase() {

    abstract fun dailyDataDao(): PlanDao
}