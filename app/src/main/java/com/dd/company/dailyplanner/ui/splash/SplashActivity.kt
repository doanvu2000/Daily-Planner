package com.dd.company.dailyplanner.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dd.company.dailyplanner.databinding.ActivitySplashBinding
import com.dd.company.dailyplanner.ui.login.LoginActivity
import com.dd.company.dailyplanner.utils.openActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            openActivity(LoginActivity::class.java)
        }, 2000)
    }
}