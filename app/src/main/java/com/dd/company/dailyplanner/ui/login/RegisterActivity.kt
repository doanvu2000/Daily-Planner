package com.dd.company.dailyplanner.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dd.company.dailyplanner.data.api.AccountBody
import com.dd.company.dailyplanner.data.api.PlanService
import com.dd.company.dailyplanner.data.api.RetrofitClient
import com.dd.company.dailyplanner.databinding.ActivityRegisterBinding
import com.dd.company.dailyplanner.utils.SharePreferenceUtil
import com.dd.company.dailyplanner.utils.enqueueShort
import com.dd.company.dailyplanner.utils.setOnSafeClick
import com.dd.company.dailyplanner.utils.showToast

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() {
        binding.btnRegister.setOnSafeClick {
            if (isValidate()) {
                RetrofitClient.getInstance().create(PlanService::class.java)
                    .registerAccount(
                        AccountBody("", binding.edtGmail.text.toString(), "")
                    ).enqueueShort(success = {
                        if (it.body()?.status == true) {
                            SharePreferenceUtil.save(
                                binding.edtGmail.text.toString(),
                                binding.edtPassword.text.toString()
                            )
                            showToast("Đăng ký thành công")
                            finish()
                        } else {
                            showToast("Đăng ký thất bại!")
                        }
                    }, failed = {

                    })
            }
        }
    }

    private fun isValidate(): Boolean {
        val email = binding.edtGmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val rePassword = binding.edtRePassword.text.toString()
        if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            showToast("Các trường không được để trống")
            return false
        }
        if (password != rePassword) {
            showToast("2 mật khẩu phải giống nhau")
            return false
        }
        return true
    }
}