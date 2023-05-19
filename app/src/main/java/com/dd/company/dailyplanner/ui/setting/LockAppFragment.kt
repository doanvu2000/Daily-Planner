package com.dd.company.dailyplanner.ui.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dd.company.dailyplanner.databinding.LayoutLockBinding
import com.dd.company.dailyplanner.ui.base.BaseFragment

class LockAppFragment : BaseFragment<LayoutLockBinding>() {
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
    ): LayoutLockBinding {
        return LayoutLockBinding.inflate(inflater)
    }
}