package com.dd.company.dailyplanner.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.data.WeekEntity
import com.dd.company.dailyplanner.data.api.PlanService
import com.dd.company.dailyplanner.data.api.RetrofitClient
import com.dd.company.dailyplanner.databinding.ActivityMainBinding
import com.dd.company.dailyplanner.ui.addplan.AddPlanActivity
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.utils.*
import okhttp3.internal.filterList
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding>() {
    companion object {
        const val COLOR_BLACK = "#000000"
        const val COLOR_TEXT_YEAR = "#59AAD7"
    }

    private var listPlan: MutableList<PlanEntity> = mutableListOf()
    private val weekAdapter by lazy {
        WeekAdapter()
    }
    private val planAdapter by lazy {
        PlanAdapter()
    }

    override fun initView() {
        setUpWeeklyView()
        setUpListPlan()
    }

    private fun setUpListPlan() {
        binding.rcvPlan.setGridManager(this, 1, planAdapter)
    }

    private fun setUpWeeklyView() {
        binding.rcvWeekly.setGridManager(this, 7, weekAdapter)
    }

    override fun initData() {
        resetTitleToDay()
        initDataWeekly()
        initDataPlan()
    }

    private val apiService by lazy {
        RetrofitClient.getInstance().create(PlanService::class.java)
    }

    private fun initDataPlan() {
        val email = "huannd0101@gmail.com"
        apiService.getPlan(email).enqueueShort(success = {
            val body = it.body()
            if (body?.status == true) {
                listPlan.clear()
                listPlan.addAll(body.data)
                updateUiWeek()
                setDataList()
            }
        }, failed = {
            Log.d("doanvv", "${it.message}")
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUiWeek() {
        weekAdapter.dataList.forEach { week ->
            val listPlanMatchDay = listPlan.filter { plan ->
                DateUtil.checkMatchTime(plan.startTime, week.day, week.month, week.year) ||
                        DateUtil.checkMatchTime(plan.endTime, week.day, week.month, week.year)
            }
            if (listPlanMatchDay.isNotEmpty()) {
                week.haveMission = true
                week.icon = listPlanMatchDay.first().icon
                weekAdapter.notifyDataSetChanged()
            } else {
                weekAdapter.notifyDataSetChanged()
                week.haveMission = false
            }
        }
    }

    private fun setDataList() {
        planAdapter.setDataList(
            listPlan.filter {
                DateUtil.checkToDay(it.startTime) || DateUtil.checkToDay(it.endTime)
            }
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun initDataWeekly() {
        val format = SimpleDateFormat("MM/dd/yyyy")
        val calendar = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }
        val WEEKDAYS = arrayOf("Th2", "Th 3", "Th 4", "Th 5", "Th 6", "Th 7", "CN")
        val days = mutableMapOf<Int, String>()
        repeat(7) {
            days[it + 1] = format.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        Log.d("doanvv", "initDataWeekly: $days")
        val listData = mutableListOf<WeekEntity>()
        days.values.forEachIndexed { index, item ->
            val split = item.split("/")
            val month = split[0].toInt()
            val day = split[1].toInt()
            val year = split[2].toInt()
            val isSelected = day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            listData.add(
                WeekEntity(
                    WEEKDAYS[index],
                    day = day,
                    month = month,
                    year = year,
                    "",
                    false,
                    isSelected
                )
            )
        }
        weekAdapter.setDataList(listData)
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
            item?.let {
                val listPlanMatchDay = listPlan.filter { plan ->
                    DateUtil.checkMatchTime(plan.startTime, item.day, item.month, item.year) ||
                            DateUtil.checkMatchTime(plan.endTime, item.day, item.month, item.year)
                }
                planAdapter.setDataList(listPlanMatchDay)
            }
        }
        binding.btnAddNewPlan.setOnSafeClick {
            openActivity(AddPlanActivity::class.java)
        }
        planAdapter.onClickCheckBox = {
            //call api change data
        }
    }

    private fun resetTitleToDay() {
        val monthStr = "Tháng ${DateUtil.getMonthInt() + 1} năm "
        val yearStr = "${DateUtil.getYear()}"
        val text =
            "<font color=\"$COLOR_BLACK\">$monthStr</font><font color=\"$COLOR_TEXT_YEAR\">$yearStr</font>"
        binding.tvTimeTitle.setTextHtml(text)
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }
}