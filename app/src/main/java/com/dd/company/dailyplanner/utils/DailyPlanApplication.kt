package com.dd.company.dailyplanner.utils

import android.app.Application

class DailyPlanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SharePreferenceUtil.init(this)
    }
}