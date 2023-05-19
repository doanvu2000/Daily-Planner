package com.dd.company.dailyplanner.utils

import android.content.Context
import android.content.SharedPreferences

object SharePreferenceUtil {
    const val EMAIL_LOGIN = "email_login"
    const val START_DAY = "startday"

    fun init(context: Context) {
        sharePref = context.getSharedPreferences("Plan", Context.MODE_PRIVATE)
    }

    lateinit var sharePref: SharedPreferences

    fun save(key: String, value: String) {
        sharePref.edit().putString(key, value).apply()
    }

    fun get(key: String): String {
        return sharePref.getString(key, "")?.trim() ?: ""
    }

    fun setStartDayOfWeek(i:Int){
        sharePref.edit().putInt(START_DAY, i).apply()
    }

    fun getStartDayOfWeek():Int = sharePref.getInt(START_DAY,0)
}