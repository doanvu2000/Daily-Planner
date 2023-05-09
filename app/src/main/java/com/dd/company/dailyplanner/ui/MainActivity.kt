package com.dd.company.dailyplanner.ui

import android.view.LayoutInflater
import com.dd.company.dailyplanner.databinding.ActivityMainBinding
import com.dd.company.dailyplanner.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun initView() {

    }

    override fun initData() {

    }

    override fun initListener() {

    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }
}