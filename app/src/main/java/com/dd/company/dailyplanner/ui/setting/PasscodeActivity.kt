package com.dd.company.dailyplanner.ui.setting

import android.view.LayoutInflater
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.databinding.LayoutLockBinding
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.utils.SharePreferenceUtil
import com.dd.company.dailyplanner.utils.showToast

class PasscodeActivity : BaseActivity<LayoutLockBinding>() {

    private var passCode = ""
    private var passCodeConfirm = ""
    
    override fun initView() {
        
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
//        binding.llDeletePasscode.setOnClickListener {
//            SharePreferenceUtils.setPassCode(this,"")
//            onBackPressed()
//        }

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

    override fun inflateViewBinding(inflater: LayoutInflater): LayoutLockBinding {
        return LayoutLockBinding.inflate(inflater)
    }

    private var isConfirm = false
    private fun clickNumber(number : Int) {
        if (!isConfirm) {
            if (number != -1) {
                if (passCode.length == 4) {
                    return
                }
                passCode += number
            } else {
                passCode = passCode.substring(0, passCode.length -1)
            }
            showPass(passCode)
            if (passCode.length == 4) {
                isConfirm = true
                passCodeConfirm = ""
                showPass(passCodeConfirm)
                binding.tvTitle.text = "Xác nhận mật khẩu"
            }
        } else {
            if (number != -1) {
                if (passCodeConfirm.length == 4) {
                    return
                }
                passCodeConfirm += number
            } else {
                passCodeConfirm = passCodeConfirm.substring(0, passCodeConfirm.length -1)
            }
            showPass(passCodeConfirm)
            if (passCodeConfirm.length == 4) {
                if (passCode == passCodeConfirm) {
                    SharePreferenceUtil.setPassCode(passCode)
                    showToast("Đặt mật khẩu thành công")
                    onBackPressed()
                } else {
                    showToast("Mật khẩu không trùng nhau")
                }
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