package com.dd.company.dailyplanner.ui.inbox

import android.view.LayoutInflater
import com.dd.company.dailyplanner.databinding.ActivityInboxBinding
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.utils.setOnSafeClick

class InboxActivity : BaseActivity<ActivityInboxBinding>() {
    override fun initView() {

    }

    override fun initData() {

    }

    override fun initListener() {
        binding.btnClose.setOnSafeClick { finish() }
        binding.btnAddnewInbox.setOnSafeClick {

        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityInboxBinding {
        return ActivityInboxBinding.inflate(inflater)
    }
}