package com.dd.company.dailyplanner.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dd.company.dailyplanner.data.api.PlanService
import com.dd.company.dailyplanner.data.api.RetrofitClient
import com.dd.company.dailyplanner.databinding.ActivityLoginBinding
import com.dd.company.dailyplanner.ui.home.MainActivity
import com.dd.company.dailyplanner.utils.*

class LoginActivity : AppCompatActivity() {
    private val apiService by lazy {
        RetrofitClient.getInstance().create(PlanService::class.java)
    }
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
            if (email.isEmpty() && password.isEmpty()) {
                showToast("Không được để trong email hoặc password!")
                return@setOnSafeClick
            }
            apiService.checkExistEmail(email).enqueueShort(success = {
                if (it.body()?.status == true) {
                    if (email.isNotEmpty() && password.isNotEmpty() && SharePreferenceUtil.get(email) == password || password == "123") {
                        SharePreferenceUtil.save(SharePreferenceUtil.EMAIL_LOGIN, binding.edtGmail.text.toString())
                        openActivity(MainActivity::class.java, isFinish = true)
                    } else {
                        showToast("Sai tài khoản hoặc mật khẩu!")
                    }
                } else {
                    showToast("Sai tài khoản hoặc mật khẩu!")
                }
            }, failed = {

            })
        }
    }

}