package com.dd.company.dailyplanner.ui.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dd.company.dailyplanner.databinding.LayoutAdvanceSettingBinding
import com.dd.company.dailyplanner.databinding.LayoutNotificationSettingBinding
import com.dd.company.dailyplanner.ui.base.BaseFragment

class AdvanceSettingFrag: BaseFragment<LayoutAdvanceSettingBinding>() {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.toolbar.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LayoutAdvanceSettingBinding {
        return LayoutAdvanceSettingBinding.inflate(inflater)
    }
}