package com.dd.company.dailyplanner.ui.addplan

import android.view.LayoutInflater
import com.dd.company.dailyplanner.databinding.ActivityAddPlanBinding
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.utils.setTextHtml

class AddPlanActivity : BaseActivity<ActivityAddPlanBinding>() {
    companion object {
        const val COLOR_BLACK = "#000000"
        const val COLOR_TEXT_YEAR = "#59AAD7"
    }

    override fun initView() {
        val text =
            "<font color=\"$COLOR_BLACK\">Tạo</font><font color=\"$COLOR_TEXT_YEAR\"> nhiệm vụ</font>"
        binding.imgTitle.setTextHtml(text)
    }

    override fun initData() {

    }

    override fun initListener() {
        binding.btnClose.setOnClickListener { finish() }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityAddPlanBinding {
        return ActivityAddPlanBinding.inflate(inflater)
    }
}