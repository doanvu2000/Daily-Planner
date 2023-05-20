package com.dd.company.dailyplanner.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.databinding.ActivitySplashBinding
import com.dd.company.dailyplanner.ui.login.LoginActivity
import com.dd.company.dailyplanner.utils.SharePreferenceUtil
import com.dd.company.dailyplanner.utils.openActivity
import com.dd.company.dailyplanner.utils.show
import com.dd.company.dailyplanner.utils.showToast

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            if (SharePreferenceUtil.getPassCode() != "") {
                binding.llPasscode.show()
            } else {
                startMain()
            }
        }, 2000)
        initListener()

    }

    private fun startMain() {
        openActivity(LoginActivity::class.java, isFinish = true)
    }


    fun initListener() {
        binding.number1.setOnClickListener { clickNumber(1) }
        binding.number2.setOnClickListener { clickNumber(2) }
        binding.number3.setOnClickListener { clickNumber(3) }
        binding.number4.setOnClickListener { clickNumber(4) }
        binding.number5.setOnClickListener { clickNumber(5) }
        binding.number6.setOnClickListener { clickNumber(6) }
        binding.number7.setOnClickListener { clickNumber(7) }
        binding.number8.setOnClickListener { clickNumber(8) }
        binding.number9.setOnClickListener { clickNumber(9) }
        binding.number0.setOnClickListener { clickNumber(0) }
        binding.imgDelete.setOnClickListener { clickNumber(-1) }
    }

    private var passCode = ""
    private fun clickNumber(number: Int) {
        if (number != -1) {
            if (passCode.length == 4) {
                return
            }
            passCode += number
        } else {
            passCode = passCode.substring(0, passCode.length - 1)
        }
        showPass(passCode)
        if (passCode.length == 4) {
            if (passCode == SharePreferenceUtil.getPassCode()) {
                startMain()
            } else {
                showToast("Passcode incorrect")
                passCode = ""
                showPass(passCode)
            }
        }
    }

    private fun showPass(passCode: String) {
        when (passCode.length) {
            0 -> {
                binding.passcode1.setImageResource(R.drawable.ic_round_radio_button_unchecked)
                binding.passcode2.setImageResource(R.drawable.ic_round_radio_button_unchecked)
                binding.passcode3.setImageResource(R.drawable.ic_round_radio_button_unchecked)
                binding.passcode4.setImageResource(R.drawable.ic_round_radio_button_unchecked)
            }
            1 -> {
                binding.passcode1.setImageResource(R.drawable.ic_round_radio_button_checked)
                binding.passcode2.setImageResource(R.drawable.ic_round_radio_button_unchecked)
                binding.passcode3.setImageResource(R.drawable.ic_round_radio_button_unchecked)
                binding.passcode4.setImageResource(R.drawable.ic_round_radio_button_unchecked)
            }
            2 -> {
                binding.passcode1.setImageResource(R.drawable.ic_round_radio_button_checked)
                binding.passcode2.setImageResource(R.drawable.ic_round_radio_button_checked)
                binding.passcode3.setImageResource(R.drawable.ic_round_radio_button_unchecked)
                binding.passcode4.setImageResource(R.drawable.ic_round_radio_button_unchecked)
            }
            3 -> {
                binding.passcode1.setImageResource(R.drawable.ic_round_radio_button_checked)
                binding.passcode2.setImageResource(R.drawable.ic_round_radio_button_checked)
                binding.passcode3.setImageResource(R.drawable.ic_round_radio_button_checked)
                binding.passcode4.setImageResource(R.drawable.ic_round_radio_button_unchecked)
            }
            4 -> {
                binding.passcode1.setImageResource(R.drawable.ic_round_radio_button_checked)
                binding.passcode2.setImageResource(R.drawable.ic_round_radio_button_checked)
                binding.passcode3.setImageResource(R.drawable.ic_round_radio_button_checked)
                binding.passcode4.setImageResource(R.drawable.ic_round_radio_button_checked)
            }
        }
    }
}