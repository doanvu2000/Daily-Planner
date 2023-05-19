package com.dd.company.dailyplanner.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.data.WeekEntity
import com.dd.company.dailyplanner.data.api.PlanService
import com.dd.company.dailyplanner.data.api.RetrofitClient
import com.dd.company.dailyplanner.databinding.ActivityMainBinding
import com.dd.company.dailyplanner.ui.addplan.AddPlanActivity
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.utils.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding>() {
    companion object {
        const val COLOR_BLACK = "#000000"
        const val COLOR_TEXT_YEAR = "#59AAD7"
        const val TYPE_PLAN = 0
        const val TYPE_INBOX = 1
    }

    private var daySelect = 0
    private var monthSelect = 0
    private var yearSelect = 0

    private var listPlan: MutableList<PlanEntity> = mutableListOf()
    private val weekAdapter by lazy {
        WeekAdapter()
    }
    private val planAdapter by lazy {
        PlanAdapter()
    }
    private val dialogPickDate by lazy {
        DialogPickDate(this)
    }
    private val email by lazy {
        SharePreferenceUtil.get(SharePreferenceUtil.EMAIL_LOGIN)
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
        val calendar = Calendar.getInstance()
        daySelect = calendar.get(Calendar.DAY_OF_MONTH)
        monthSelect = calendar.get(Calendar.MONTH)
        yearSelect = calendar.get(Calendar.YEAR)
        resetTitleToDay()
        initDataWeekly()
        initDataPlan()
    }

    private val apiService by lazy {
        RetrofitClient.getInstance().create(PlanService::class.java)
    }

    override fun onResume() {
        super.onResume()
        initDataPlan()
    }

    private fun initDataPlan() {
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
                plan.type == TYPE_PLAN &&
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
                it.type == TYPE_PLAN && DateUtil.checkToDay(it.startTime) || DateUtil.checkToDay(it.endTime)
            }
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun initDataWeekly() {
        val format = SimpleDateFormat("MM/dd/yyyy")
        val calendar = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.YEAR, yearSelect)
            set(Calendar.MONTH, monthSelect)
            set(Calendar.DAY_OF_MONTH, daySelect)
        }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val WEEKDAYS = arrayOf("Th2", "Th 3", "Th 4", "Th 5", "Th 6", "Th 7", "CN")
        val days = mutableMapOf<Int, String>()
        val space = daySelect - dayOfWeek
        repeat(7) {
            calendar.set(Calendar.DAY_OF_MONTH, it + 1 + space)
            days[it + 1] = format.format(calendar.time)
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
        updateUiWeek()
        setSelectDay(dayOfWeek - 1, weekAdapter.dataList[dayOfWeek - 1])
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        binding.tvTimeTitle.setOnSafeClick {
            resetTitleToDay()
        }
        binding.btnCalendar.setOnSafeClick {
            showDialogPickDate()
        }
        binding.btnInbox.setOnSafeClick { }
        binding.btnSetting.setOnSafeClick { }
        weekAdapter.setOnClickItem { item, position ->
            setSelectDay(position, item)
        }
        binding.btnAddNewPlan.setOnSafeClick {
            openActivity(
                AddPlanActivity::class.java, bundle = bundleOf(
                    "day" to daySelect, "month" to monthSelect, "year" to yearSelect
                )
            )
        }
        planAdapter.onClickCheckBox = { plan ->
            //call api change data
            val findPlan = listPlan.find { it.id == plan.id }
            findPlan?.isDone = plan.isDone
            apiService.syncPlan(email, listPlan).enqueueShort(success = {
                Log.d("doanvv", "planId ${plan.id} is Done")
            }, failed = {
                showToast("Failed to update status plan")
            })
        }
    }

    private fun setSelectDay(position: Int, item: WeekEntity?) {
        weekAdapter.setSelected(position)
        item?.let {
            daySelect = it.day
            monthSelect = it.month
            yearSelect = it.year
            val listPlanMatchDay = listPlan.filter { plan ->
                DateUtil.checkMatchTime(plan.startTime, item.day, item.month, item.year) ||
                        DateUtil.checkMatchTime(plan.endTime, item.day, item.month, item.year)
            }
            planAdapter.setDataList(listPlanMatchDay)
        }
    }

    private fun showDialogPickDate() {
        if (!dialogPickDate.isShowing()) {
            dialogPickDate.show(
                daySelect, monthSelect, yearSelect, true, onClickSubmit = { day, month, year ->
                    daySelect = day
                    monthSelect = month
                    yearSelect = year
                    initDataWeekly()
                }
            )
        }
    }

    private fun resetTitleToDay() {
        val monthStr = "Tháng ${DateUtil.getMonthInt() + 1} năm "
        val yearStr = "${DateUtil.getYear()}"
        val text =
            "<font color=\"$COLOR_BLACK\">$monthStr</font><font color=\"$COLOR_TEXT_YEAR\">$yearStr</font>"
        binding.tvTimeTitle.setTextHtml(text)
    }

    override fun onBackPressed() {
        if (dialogPickDate.isShowing()) {
            dialogPickDate.hide()
        } else {
            super.onBackPressed()
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }
}