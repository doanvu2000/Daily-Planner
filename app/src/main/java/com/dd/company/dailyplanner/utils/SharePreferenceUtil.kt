package com.dd.company.dailyplanner.utils

import android.content.Context
import android.content.SharedPreferences

object SharePreferenceUtil {
    const val EMAIL_LOGIN = "email_login"

    fun init(context: Context) {
        sharePref = context.getSharedPreferences("Plan", Context.MODE_PRIVATE)
    }

    lateinit var sharePref: SharedPreferences

    fun save(key: String, value: String) {
        sharePref.edit().putString(key, value).apply()
    }

    fun get(key: String): String {
        return sharePref.getString(key, "") ?: ""
    }
}