package com.dd.company.dailyplanner.ui.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.AlarmManagerCompat
import com.dd.company.dailyplanner.databinding.LayoutNotificationSettingBinding
import com.dd.company.dailyplanner.ui.base.BaseFragment
import com.dd.company.dailyplanner.ui.setting.notify.AlarmReceiver

class SettingNotificationFrag:BaseFragment<LayoutNotificationSettingBinding>() {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.toolbar.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.swNotify.setOnCheckedChangeListener { compoundButton, b ->
            if (b){

            }
        }
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LayoutNotificationSettingBinding {
        return LayoutNotificationSettingBinding.inflate(inflater)
    }
}