package com.dd.company.dailyplanner.ui.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.databinding.LayoutAdvanceSettingBinding
import com.dd.company.dailyplanner.databinding.LayoutNotificationSettingBinding
import com.dd.company.dailyplanner.ui.base.BaseFragment
import com.dd.company.dailyplanner.utils.SharePreferenceUtil
import com.dd.company.dailyplanner.utils.setOnSafeClick
import com.dd.company.dailyplanner.utils.showDialogPickOptionValue

class AdvanceSettingFrag: BaseFragment<LayoutAdvanceSettingBinding>() {
    override fun initView() {
        val data: List<String> = resources.getStringArray(R.array.day_of_week_value).toList()
        val index = SharePreferenceUtil.getStartDayOfWeek()
        binding.tvDate.text = data[index]
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.toolbar.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.layoutStartDayOfWeek.setOnSafeClick {
            requireContext().showDialogPickOptionValue { s, i ->
                binding.tvDate.text = s
                SharePreferenceUtil.setStartDayOfWeek(i)
            }
        }
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LayoutAdvanceSettingBinding {
        return LayoutAdvanceSettingBinding.inflate(inflater)
    }
}