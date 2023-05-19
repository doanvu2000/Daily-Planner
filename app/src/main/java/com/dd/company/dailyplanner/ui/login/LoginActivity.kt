package com.dd.company.dailyplanner.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dd.company.dailyplanner.databinding.ActivityLoginBinding
import com.dd.company.dailyplanner.ui.home.MainActivity
import com.dd.company.dailyplanner.utils.SharePreferenceUtil
import com.dd.company.dailyplanner.utils.openActivity
import com.dd.company.dailyplanner.utils.setOnSafeClick
import com.dd.company.dailyplanner.utils.showToast

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() {
        binding.btnRegister.setOnSafeClick {
            openActivity(RegisterActivity::class.java)
        }
        binding.btnLogin.setOnSafeClick {
            val email = binding.edtGmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty() && SharePreferenceUtil.get(email) == password) {
                SharePreferenceUtil.save(SharePreferenceUtil.EMAIL_LOGIN, binding.edtGmail.text.toString())
                openActivity(MainActivity::class.java)
            } else {
                showToast("Sai tài khoản hoặc mật khẩu!")
            }
        }
    }

}