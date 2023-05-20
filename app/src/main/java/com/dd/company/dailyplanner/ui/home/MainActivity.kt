package com.dd.company.dailyplanner.ui.home

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.data.WeekEntity
import com.dd.company.dailyplanner.data.api.PlanService
import com.dd.company.dailyplanner.data.api.RetrofitClient
import com.dd.company.dailyplanner.databinding.ActivityMainBinding
import com.dd.company.dailyplanner.ui.addplan.AddPlanActivity
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.ui.copyplan.CopyPlanActivity
import com.dd.company.dailyplanner.ui.editplan.EditPlanActivity
import com.dd.company.dailyplanner.ui.inbox.InboxActivity
import com.dd.company.dailyplanner.ui.setting.SettingActivity
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

    private var planSelected: PlanEntity? = null
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
    private val dialogConfirmRemovePlan by lazy {
        DialogConfirmRemovePlan(this)
    }
    private val email by lazy {
        SharePreferenceUtil.get(SharePreferenceUtil.EMAIL_LOGIN).trim()
    }

    override fun initView() {
        setUpWeeklyView()
        setUpListPlan()
        binding.tvDescriptionPlan.isSelected = true
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
        resetTitleToDay(DateUtil.getMonthInt() + 1, DateUtil.getYear())
        initDataWeekly()
        initDataPlan()
    }

    private val apiService by lazy {
        RetrofitClient.getInstance().create(PlanService::class.java)
    }

    override fun onResume() {
        super.onResume()
        initData()
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
            } else {
                week.haveMission = false
            }
        }
        weekAdapter.notifyDataSetChanged()
    }

    private fun setDataList() {
        planAdapter.setDataList(
            listPlan.filter {
                it.type == TYPE_PLAN && DateUtil.checkMatchTime(
                    it.startTime,
                    daySelect,
                    monthSelect,
                    yearSelect
                ) || DateUtil.checkMatchTime(
                    it.endTime,
                    daySelect,
                    monthSelect,
                    yearSelect
                )
            }
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun initDataWeekly() {
        val dayStartOfWeek = DayWeek.getDayByIndex(SharePreferenceUtil.getStartDayOfWeek() + 2)
        val format = SimpleDateFormat("MM/dd/yyyy")
        val calendar = Calendar.getInstance().apply {
            firstDayOfWeek = dayStartOfWeek.index
            set(Calendar.DAY_OF_WEEK, dayStartOfWeek.index)
            set(Calendar.YEAR, yearSelect)
            set(Calendar.MONTH, monthSelect)
            set(Calendar.DAY_OF_MONTH, daySelect)
        }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val fullValueDay = DayWeek.getDayByIndex(dayOfWeek).value
        val WEEKDAYS = DayWeek.getTitleWeek().toMutableList()
        when (dayStartOfWeek) {
            DayWeek.Monday -> Collections.rotate(WEEKDAYS, -1)
            DayWeek.Tuesday -> Collections.rotate(WEEKDAYS, -2)
            DayWeek.Wednesday -> Collections.rotate(WEEKDAYS, -3)
            DayWeek.Thursday -> Collections.rotate(WEEKDAYS, -4)
            DayWeek.Friday -> Collections.rotate(WEEKDAYS, -5)
            DayWeek.Saturday -> Collections.rotate(WEEKDAYS, -6)
            else -> {}
        }
        val days = mutableMapOf<Int, String>()
        val space = daySelect - WEEKDAYS.indexOf(fullValueDay) - 1
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
            val isSelected = day == daySelect
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
        setSelectDay(daySelect)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        binding.tvTimeTitle.setOnSafeClick {
            resetTitleToDay(DateUtil.getMonthInt() + 1, DateUtil.getYear())
            initData()
        }
        binding.btnCalendar.setOnSafeClick {
            showDialogPickDate()
        }
        binding.btnInbox.setOnSafeClick {
            openActivity(InboxActivity::class.java)
        }
        binding.btnSetting.setOnSafeClick {
            openActivity(SettingActivity::class.java)
        }
        weekAdapter.setOnClickItem { item, position ->
            setSelectDay(item?.day ?: DateUtil.getDayInt())
//            setSelectDay(position, item)
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
            syncPlan()
            planSelected = plan
        }
        planAdapter.setOnClickItem { item, position ->
            planSelected = item
            showEditBottom(item)
        }
        binding.btnCloseEdit.setOnSafeClick {
            hideLayoutEdit()
        }
        binding.btnRemovePlan.setOnSafeClick {
            //remove plan
            showDialogConfirmRemove()
        }
        binding.btnCopyPlan.setOnSafeClick {
            //copy plan
            hideLayoutEdit()
            openActivity(CopyPlanActivity::class.java, bundleOf("plan_entity" to planSelected))
        }
        binding.btnDonePlan.setOnSafeClick {
            //done plan
            planSelected?.isDone = !(planSelected?.isDone ?: false)
            val findPlan = listPlan.find { it.id == planSelected?.id }
            findPlan?.isDone = planSelected?.isDone ?: false
            planSelected?.let { syncPlan() }
            hideLayoutEdit()
        }
        binding.btnEditPlan.setOnSafeClick {
            //edit plan
            hideLayoutEdit()
            openActivity(EditPlanActivity::class.java, bundleOf("plan_entity" to planSelected))
        }
    }

    private fun showDialogConfirmRemove() {
        if (!dialogConfirmRemovePlan.isShowing()) {
            dialogConfirmRemovePlan.show {
                listPlan.remove(planSelected)
                syncPlan("Nhiệm vụ đã xóa!")
                hideLayoutEdit()
            }
        }
    }

    private fun hideLayoutEdit() {
        binding.layoutBottomEdit.gone()
        binding.btnAddNewPlan.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun syncPlan(msg: String? = null) {
        showLoading()
        listPlan.forEach {
            if (it.name.isEmpty()) {
                it.name = "."
            }
        }
        apiService.syncPlan(email, listPlan).enqueueShort(success = {
            hideLoading()
            if (it.code() == 200) {
                initDataPlan()
                msg?.let {
                    showToast(it)
                }
            } else {
                showToast("${it.raw()}")
            }
        }, failed = {
            hideLoading()
            showToast("${it.message}")
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showEditBottom(item: PlanEntity?) {
        item?.let {
            binding.layoutBottomEdit.show()
            binding.btnAddNewPlan.gone()
            binding.imgIconPlan.setImageResource(getDrawableIdByName(it.icon))
            val timeStart = DateUtil.getHourMinuteFormatFromLong(it.startTime)
            val timeEnd = DateUtil.getHourMinuteFormatFromLong(it.endTime)
            val date = DateUtil.getDateFormatFromLong(it.startTime)
            val diffTime = DateUtil.diffTime(it.startTime, it.endTime)
            binding.tvTimeCountPlan.text = "$timeStart-$timeEnd, $date ($diffTime)"
            binding.tvDescriptionPlan.apply {
                text = it.content
                if (it.isDone) {
                    paintFlags = binding.tvTimeTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.btnDonePlan.setImageResource(R.drawable.layout_not_done_plan)
                } else {
                    paintFlags = binding.tvTimeTitle.paintFlags
                    binding.btnDonePlan.setImageResource(R.drawable.layout_done_plan)
                }
            }
        }
    }

    private fun setSelectDay(day: Int) {
        val index = weekAdapter.dataList.indexOfLast { it.day == day }
        weekAdapter.setSelected(index)
        val item = weekAdapter.dataList.find { it.day == day }
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
                daySelect, monthSelect - 1, yearSelect, true, onClickSubmit = { day, month, year ->
                    daySelect = day
                    monthSelect = month
                    yearSelect = year
                    resetTitleToDay(monthSelect + 1, year)
                    initDataWeekly()
                }
            )
        }
    }

    private fun resetTitleToDay(month: Int, year: Int) {
        val monthStr = "Tháng $month năm "
        val yearStr = "$year"
        val text =
            "<font color=\"$COLOR_BLACK\">$monthStr</font><font color=\"$COLOR_TEXT_YEAR\">$yearStr</font>"
        binding.tvTimeTitle.setTextHtml(text)
    }

    override fun onBackPressed() {
        if (dialogPickDate.isShowing()) {
            dialogPickDate.hide()
        } else if (dialogConfirmRemovePlan.isShowing()) {
            dialogConfirmRemovePlan.hide()
        } else {
            super.onBackPressed()
        }
    }

    private fun showLoading() {
        binding.loading.show()
    }

    private fun hideLoading() {
        binding.loading.gone()
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }
}