package com.dd.company.dailyplanner.ui.addplan

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.data.api.PlanService
import com.dd.company.dailyplanner.data.api.RetrofitClient
import com.dd.company.dailyplanner.databinding.ActivityAddPlanBinding
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.ui.setting.notify.setReminder
import com.dd.company.dailyplanner.utils.*
import java.util.*

class AddPlanActivity : BaseActivity<ActivityAddPlanBinding>() {
    companion object {
        const val COLOR_BLACK = "#000000"
        const val COLOR_TEXT_YEAR = "#59AAD7"
        const val LOOP_ONE = 1
        const val LOOP_DAY = 2
        const val LOOP_WEEK = 3
        const val LOOP_MONTH = 4

        const val TYPE_INBOX = 1
        const val TYPE_PLAN = 0
    }

    private val email by lazy {
        SharePreferenceUtil.get(SharePreferenceUtil.EMAIL_LOGIN).trim()
    }
    private val day by lazy {
        intent?.extras?.getInt("day") ?: 0
    }
    private val month by lazy {
        intent?.extras?.getInt("month") ?: 0
    }
    private val year by lazy {
        intent?.extras?.getInt("year") ?: 0
    }

    private var isNotifyStartPlan = true
    private var isNotifyEndPlan = false

    private var imagePlan = "ic_email"
    private var loopType: Int = 1
    private var lastSelectedHourStart = 8
    private var lastSelectedMinuteStart = 0
    private var lastSelectedHourEnd = 8
    private var lastSelectedMinuteEnd = 0
    private var isSelectIcon = false
    private val dialogChooseIcon by lazy {
        DialogChooseIcon(this)
    }
    private val planEntity by lazy {
        PlanEntity()
    }
    private val apiService by lazy {
        RetrofitClient.getInstance().create(PlanService::class.java)
    }
    private val newPlanEntity by lazy {
        PlanEntity()
    }
    private var listPlan: MutableList<PlanEntity> = mutableListOf()

    override fun initView() {
        val str1 = "Tạo"
        val str2 = " nhiệm vụ"
        val text =
            "<font color=\"$COLOR_BLACK\">$str1</font><font color=\"$COLOR_TEXT_YEAR\">$str2</font>"
        binding.imgTitle.setTextHtml(text)
    }

    override fun initData() {
        apiService.getPlan(email).enqueueShort(success = {
            val body = it.body()
            if (body?.status == true) {
                listPlan.clear()
                listPlan.addAll(body.data)
            }
        }, failed = {
            Log.d("doanvv", "${it.message}")
        })
    }

    override fun initListener() {
        binding.btnClose.setOnClickListener { finish() }
        val basePlans = listOf(binding.layoutEmail, binding.layoutRunning, binding.layoutWatchFilm)
        val baseTitle = listOf("Trả lời Emails", "Chạy bộ", "Xem phim")
        val baseIcon = listOf(R.drawable.ic_email, R.drawable.ic_running, R.drawable.ic_watch_tv)
        val baseIconName = listOf("ic_email", "ic_running", "ic_watch_tv")
        basePlans.forEachIndexed { index, constraintLayout ->
            constraintLayout.setOnClickListener {
                binding.edtTitlePlan.apply {
                    setText(baseTitle[index])
                    setSelection(baseTitle[index].length)
                }
                if (!isSelectIcon) {
                    planEntity.icon = baseIconName[index]
                    binding.imgIconPlan.setImageResource(baseIcon[index])
                    imagePlan = baseIconName[index]
                }
                hideAllBasePlan()
            }
        }
        binding.edtTitlePlan.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                showAllBasePlan()
                if (!isSelectIcon) {
                    binding.imgIconPlan.setImageResource(baseIcon[0])
                }
            } else {
                hideAllBasePlan()
            }
        }
        binding.imgIconPlan.setOnClickListener {
            showDialogChooseIcon()
        }
        binding.tvStartTime.setOnClickListener {
            showDialogTime()
        }
        binding.tvEndTime.setOnClickListener {
            showDialogTimeEnd()
        }
        binding.btnAddToBox.setOnClickListener {
            newPlanEntity.type = TYPE_INBOX
            binding.layoutSetTime.gone()
            binding.layoutInbox.show()
            binding.btnAddToBox.gone()
            binding.btnAddTime.show()
            binding.tvNeedNotification.gone()
            binding.layoutNotification.gone()
        }
        binding.btnAddTime.setOnClickListener {
            newPlanEntity.type = TYPE_PLAN
            binding.layoutSetTime.show()
            binding.layoutInbox.gone()
            binding.btnAddToBox.show()
            binding.btnAddTime.gone()
            binding.tvNeedNotification.show()
            binding.layoutNotification.show()
        }
        binding.tvLoopOne.setOnSafeClick {
            setUpViewLoop(LOOP_ONE)
        }
        binding.tvLoopDay.setOnSafeClick {
            setUpViewLoop(LOOP_DAY)
        }
        binding.tvLoopWeek.setOnSafeClick {
            setUpViewLoop(LOOP_WEEK)
        }
        binding.tvLoopMonth.setOnSafeClick {
            setUpViewLoop(LOOP_MONTH)
        }
        binding.imgCloseStart.setOnClickListener {
            isNotifyStartPlan = !isNotifyStartPlan
            setUpCloseIconNotify()
        }
        binding.imgCloseEnd.setOnClickListener {
            isNotifyEndPlan = !isNotifyEndPlan
            setUpCloseIconNotify()
        }
        binding.btnCreate.setOnClickListener {
            checkValidateAndUpload()
        }
    }

    private fun checkValidateAndUpload() {
        //image plan
        val msg = "Không hợp lệ"
        newPlanEntity.icon = imagePlan
        //content plan
        val title = binding.edtTitlePlan.text.toString()
        if (title.isEmpty()) {
            showToast(msg)
            return
        }
        newPlanEntity.content = title
        //loop type: 1-> create 1, 2 -> create all month, 3 -> create 1 in week, 4-> create 1 in month
        //time start
        if (newPlanEntity.type == TYPE_PLAN) {//if plan, save time start, end
            if (binding.tvStartTime.text.isNullOrEmpty() || binding.tvEndTime.text.isNullOrEmpty()) {
                showToast(msg)
                return
            }
            val timeStart = Calendar.getInstance().apply {
                set(year, month - 1, day, lastSelectedHourStart, lastSelectedMinuteStart)
            }.timeInMillis
            val timeEnd = Calendar.getInstance().apply {
                set(year, month - 1, day, lastSelectedHourEnd, lastSelectedMinuteEnd)
            }.timeInMillis
            newPlanEntity.apply {
                this.startTime = timeStart
                this.endTime = timeEnd
            }
        }
        //detail description
        newPlanEntity.name = binding.edtDetailPlan.text.toString()
        when (loopType) {
            LOOP_ONE, LOOP_WEEK, LOOP_MONTH -> {
                newPlanEntity.id = -1
                listPlan.add(newPlanEntity)
                if (isNotifyStartPlan) {
                    setReminder(
                        this,
                        newPlanEntity.content,
                        DateUtil.getHourMinuteFormatFromLong(newPlanEntity.startTime),
                        newPlanEntity.startTime
                    )
                }
                if (isNotifyEndPlan) {
                    setReminder(
                        this,
                        newPlanEntity.content,
                        DateUtil.getHourMinuteFormatFromLong(newPlanEntity.endTime),
                        newPlanEntity.endTime
                    )
                }
            }
            LOOP_DAY -> {
                val dayOfMonth = when {
                    month == 2 && year % 4 == 0 && year % 100 != 0 || year % 400 == 0 -> 29
                    month == 2 -> 28
                    else -> DateUtil.getDayCountOfMonth(month) ?: 30
                }
                for (i in 1..dayOfMonth) {
                    val timeStart = Calendar.getInstance().apply {
                        set(year, month - 1, i, lastSelectedHourStart, lastSelectedMinuteStart)
                    }.timeInMillis
                    val timeEnd = Calendar.getInstance().apply {
                        set(year, month - 1, i, lastSelectedHourEnd, lastSelectedMinuteEnd)
                    }.timeInMillis
                    val plan = newPlanEntity.copy()
                    plan.startTime = timeStart
                    plan.endTime = timeEnd
                    plan.id = -i.toLong()
                    listPlan.add(plan)
                    if (isNotifyStartPlan) {
                        setReminder(
                            this,
                            plan.content,
                            DateUtil.getHourMinuteFormatFromLong(plan.startTime),
                            plan.startTime
                        )
                    }
                    if (isNotifyEndPlan) {
                        setReminder(
                            this,
                            plan.content,
                            DateUtil.getHourMinuteFormatFromLong(plan.endTime),
                            plan.endTime
                        )
                    }
                }

            }
        }
        apiService.syncPlan(email, listPlan).enqueueShort(success = {
            showToast("add plan success")
            finish()
        }, failed = {
            showToast("Error when add plan: ${it.message}")
        })
    }

    private fun setUpCloseIconNotify() {
        binding.imgCloseStart.setImageResource(if (isNotifyStartPlan) R.drawable.ic_close else R.drawable.ic_plus_notification)
        binding.imgCloseEnd.setImageResource(if (isNotifyEndPlan) R.drawable.ic_close else R.drawable.ic_plus_notification)
    }

    private fun setUpViewLoop(loop: Int) {
        loopType = loop
        val resource = R.drawable.background_button_continue
        val colorWhite = ContextCompat.getColor(this, R.color.white)
        val colorLoop = ContextCompat.getColor(this, R.color.text_loop)

        binding.tvLoopOne.setBackgroundResource(if (loop == LOOP_ONE) resource else 0)
        binding.tvLoopDay.setBackgroundResource(if (loop == LOOP_DAY) resource else 0)
        binding.tvLoopWeek.setBackgroundResource(if (loop == LOOP_WEEK) resource else 0)
        binding.tvLoopMonth.setBackgroundResource(if (loop == LOOP_MONTH) resource else 0)

        binding.tvLoopOne.setTextColor(if (loop == LOOP_ONE) colorWhite else colorLoop)
        binding.tvLoopDay.setTextColor(if (loop == LOOP_DAY) colorWhite else colorLoop)
        binding.tvLoopWeek.setTextColor(if (loop == LOOP_WEEK) colorWhite else colorLoop)
        binding.tvLoopMonth.setTextColor(if (loop == LOOP_MONTH) colorWhite else colorLoop)
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogTimeEnd() {
        val is24HView = true

        val timeSetListener = OnTimeSetListener { _, hourOfDay, minute ->
            if (isValidateTime(hourOfDay, minute)) {
                binding.tvEndTime.text =
                    "Time end: ${DateUtil.formatHourMinutes(hourOfDay, minute)}"
                lastSelectedHourEnd = hourOfDay
                lastSelectedMinuteEnd = minute
            } else {
                showToast("Require: Time end > Time start")
            }
        }
        val timePickerDialog = TimePickerDialog(
            this,
            timeSetListener, lastSelectedHourEnd, lastSelectedMinuteEnd, is24HView
        )
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogTime() {
        val is24HView = true

        val timeSetListener = OnTimeSetListener { _, hourOfDay, minute ->
            binding.tvStartTime.text =
                "Time start: ${DateUtil.formatHourMinutes(hourOfDay, minute)}"
            lastSelectedHourStart = hourOfDay
            lastSelectedMinuteStart = minute
        }
        val timePickerDialog = TimePickerDialog(
            this,
            timeSetListener, lastSelectedHourStart, lastSelectedMinuteStart, is24HView
        )
        timePickerDialog.show()
    }

    private fun isValidateTime(hourOfDay: Int, minute: Int): Boolean {
        if (hourOfDay < lastSelectedHourStart) {
            return false
        }
        if (hourOfDay > lastSelectedHourStart) {
            return true
        }
        if (minute <= lastSelectedMinuteStart) {
            return false
        }
        return true
    }

    private fun hideAllBasePlan() {
        binding.layoutBasePlan.gone()
        binding.layoutTime.show()
    }

    private fun showAllBasePlan() {
        binding.layoutBasePlan.show()
        binding.layoutTime.gone()
    }

    private fun showDialogChooseIcon() {
        if (!dialogChooseIcon.isShowing()) {
            dialogChooseIcon.show { icon, nameIcon ->
                binding.imgIconPlan.setImageResource(icon)
                isSelectIcon = true
                imagePlan = nameIcon
            }
        }
    }

    override fun onBackPressed() {
        if (dialogChooseIcon.isShowing()) {
            dialogChooseIcon.hide()
        } else {
            super.onBackPressed()
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityAddPlanBinding {
        return ActivityAddPlanBinding.inflate(inflater)
    }
}