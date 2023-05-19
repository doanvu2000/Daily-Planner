package com.dd.company.dailyplanner.ui.setting

import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.databinding.ActivitySettingBinding
import com.dd.company.dailyplanner.ui.base.BaseActivity

class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    override fun initView() {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, SettingFragment())
            .addToBackStack(SettingFragment::class.java.simpleName).commitAllowingStateLoss()
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment)
            .addToBackStack(fragment::class.java.simpleName).commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

}