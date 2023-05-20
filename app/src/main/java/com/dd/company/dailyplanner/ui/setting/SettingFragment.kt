package com.dd.company.dailyplanner.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.dd.company.dailyplanner.databinding.FragmentSettingBinding
import com.dd.company.dailyplanner.ui.base.BaseFragment
import com.dd.company.dailyplanner.utils.SharePreferenceUtil
import com.dd.company.dailyplanner.utils.openActivity

class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    var isFirstOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun initView() {

    }

    override fun initData() {
    }

    override fun initListener() {
        binding.settingNotification.setOnClickListener {
            (activity as? SettingActivity)?.addFragment(SettingNotificationFrag())
        }
        binding.advanceSetting.setOnClickListener {
            (activity as? SettingActivity)?.addFragment(AdvanceSettingFrag())
        }
        binding.toolbar.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }
    private val listener = CompoundButton.OnCheckedChangeListener { p0, b ->
        if (b) {
            if (!isFirstOpen||SharePreferenceUtil.getPassCode().isEmpty()) {
                (activity as? SettingActivity)?.openActivity(PasscodeActivity::class.java)
            } else {
                isFirstOpen = false
            }
        } else {
            SharePreferenceUtil.setPassCode("")
        }
    }


    override fun onResume() {
        super.onResume()
        binding.swPasscode.setOnCheckedChangeListener(null)
        binding.swPasscode.isChecked = SharePreferenceUtil.getPassCode().isNotEmpty()
        binding.swPasscode.setOnCheckedChangeListener(listener)
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater)
    }
}