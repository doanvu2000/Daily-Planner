package com.dd.company.dailyplanner.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dd.company.dailyplanner.databinding.FragmentSettingBinding
import com.dd.company.dailyplanner.ui.base.BaseFragment

class SettingFragment : BaseFragment<FragmentSettingBinding>() {

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
        binding.settingApp.setOnClickListener {
            (activity as? SettingActivity)?.addFragment(LockAppFragment())
        }
        binding.advanceSetting.setOnClickListener {
            (activity as? SettingActivity)?.addFragment(AdvanceSettingFrag())
        }
        binding.toolbar.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater)
    }
}