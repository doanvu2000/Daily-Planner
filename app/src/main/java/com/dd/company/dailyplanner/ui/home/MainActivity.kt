package com.dd.company.dailyplanner.ui.home

import android.view.LayoutInflater
import com.dd.company.dailyplanner.data.WeekEntity
import com.dd.company.dailyplanner.databinding.ActivityMainBinding
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.utils.DateUtil
import com.dd.company.dailyplanner.utils.setGridManager
import com.dd.company.dailyplanner.utils.setOnSafeClick
import com.dd.company.dailyplanner.utils.setTextHtml

class MainActivity : BaseActivity<ActivityMainBinding>() {
    companion object {
        const val COLOR_BLACK = "#000000"
        const val COLOR_TEXT_YEAR = "#59AAD7"
    }

    private val weekAdapter by lazy {
        WeekAdapter()
    }

    override fun initView() {
        setUpWeeklyView()
    }

    private fun setUpWeeklyView() {
        binding.rcvWeekly.setGridManager(this, 7, weekAdapter)
    }

    override fun initData() {
        resetTitleToDay()
        initDataWeekly()
    }

    private fun initDataWeekly() {
        //fake data
        weekAdapter.setDataList(
            mutableListOf(
                WeekEntity("Th 2", 8, "R.drawable.doan", true),
                WeekEntity("Th 3", 9, "R.drawable.doan", true),
                WeekEntity("Th 4", 10, "R.drawable.doan", true, isSelected = true),
                WeekEntity("Th 5", 11, "R.drawable.doan", true),
                WeekEntity("Th 6", 12, "R.drawable.doan", true),
                WeekEntity("Th 7", 13, "R.drawable.doan", true),
                WeekEntity("CN", 14, "R.drawable.doan", true),
            )
        )
    }

    override fun initListener() {
        binding.tvTimeTitle.setOnSafeClick {
            resetTitleToDay()
        }
        binding.btnCalendar.setOnSafeClick { }
        binding.btnInbox.setOnSafeClick { }
        binding.btnSetting.setOnSafeClick { }
        weekAdapter.setOnClickItem { item, position ->
            weekAdapter.setSelected(position)
        }
    }

    private fun resetTitleToDay() {
        val monthStr = "Tháng ${DateUtil.getMonthInt()} năm "
        val yearStr = "${DateUtil.getYear()}"
        val text =
            "<font color=\"$COLOR_BLACK\">$monthStr</font><font color=\"$COLOR_TEXT_YEAR\">$yearStr</font>"
        binding.tvTimeTitle.setTextHtml(text)
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }
}