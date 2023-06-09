package com.dd.company.dailyplanner.ui.copyplan

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.data.api.PlanService
import com.dd.company.dailyplanner.data.api.RetrofitClient
import com.dd.company.dailyplanner.databinding.ActivityCopyPlanBinding
import com.dd.company.dailyplanner.ui.addplan.DialogChooseIcon
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.ui.setting.notify.setReminder
import com.dd.company.dailyplanner.utils.*
import com.dd.company.dailyplanner.utils.DateUtil.getDay
import com.dd.company.dailyplanner.utils.DateUtil.getHour
import com.dd.company.dailyplanner.utils.DateUtil.getMinutes
import com.dd.company.dailyplanner.utils.DateUtil.getMonth
import com.dd.company.dailyplanner.utils.DateUtil.getYear
import java.util.*

class CopyPlanActivity : BaseActivity<ActivityCopyPlanBinding>() {
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

    private var isNotifyStartPlan = true
    private var isNotifyEndPlan = false
    private var isInbox = false

    private var imagePlan = "ic_email"
    private var loopType: Int = 1
    private var lastSelectedHourStart = 8
    private var lastSelectedMinuteStart = 0
    private var lastSelectedHourEnd = 8
    private var lastSelectedMinuteEnd = 0
    private var isSelectIcon = false
    private var day = 0
    private var month = 0
    private var year = 0
    private val dialogChooseIcon by lazy {
        DialogChooseIcon(this)
    }
    private val apiService by lazy {
        RetrofitClient.getInstance().create(PlanService::class.java)
    }
    private lateinit var newPlanEntity: PlanEntity
    private var listPlan: MutableList<PlanEntity> = mutableListOf()

    override fun initView() {
        val str1 = "Sao chép"
        val str2 = " nhiệm vụ"
        val text =
            "<font color=\"$COLOR_BLACK\">$str1</font><font color=\"$COLOR_TEXT_YEAR\">$str2</font>"
        binding.imgTitle.setTextHtml(text)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        newPlanEntity = (intent?.extras?.getSerializable("plan_entity") as? PlanEntity) ?: PlanEntity()
        binding.tvStartTime.text =
            "Thời gian bắt đầu: ${DateUtil.getHourMinuteFormatFromLong(newPlanEntity.startTime)}"
        binding.tvEndTime.text =
            "Thời gian kết thúc: ${DateUtil.getHourMinuteFormatFromLong(newPlanEntity.endTime)}"
        binding.imgIconPlan.setImageResource(getDrawableIdByName(newPlanEntity.icon))
        lastSelectedHourStart = newPlanEntity.startTime.getHour()
        lastSelectedMinuteStart = newPlanEntity.startTime.getMinutes()
        lastSelectedHourEnd = newPlanEntity.endTime.getHour()
        lastSelectedMinuteEnd = newPlanEntity.endTime.getMinutes()
        year = newPlanEntity.startTime.getYear()
        month = newPlanEntity.startTime.getMonth()
        day = newPlanEntity.startTime.getDay()
        imagePlan = newPlanEntity.icon
        isInbox = intent?.extras?.getBoolean("is_inbox") ?: false
        if (isInbox) {
            newPlanEntity.type = TYPE_INBOX
            binding.tvLoopTitle.gone()
            binding.layoutLoop.gone()
            binding.layoutSetTime.gone()
            binding.layoutInbox.show()
            binding.btnAddToBox.gone()
            binding.btnAddTime.gone()
            binding.tvNeedNotification.gone()
            binding.layoutNotification.gone()
        }
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
                    newPlanEntity.icon = baseIconName[index]
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
        binding.edtTitlePlan.setText(newPlanEntity.content)
        binding.edtDetailPlan.setText(newPlanEntity.name)
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
            if (!DateUtil.compareTwoTime(lastSelectedHourStart, lastSelectedMinuteStart, lastSelectedHourEnd, lastSelectedMinuteEnd)) {
                showToast("Thời gian kết thúc phải lớn hơn thời gian bắt đầu!")
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
        } else {
            newPlanEntity.apply {
                this.startTime = 0
                this.endTime = 0
            }
        }
        //detail description
        newPlanEntity.name = binding.edtDetailPlan.text.toString()
        when (loopType) {
            LOOP_ONE, LOOP_WEEK, LOOP_MONTH -> {
                newPlanEntity.id = -1
                listPlan.add(newPlanEntity)
                if (isNotifyStartPlan && newPlanEntity.type == TYPE_PLAN) {
                    setReminder(
                        this,
                        newPlanEntity.content,
                        DateUtil.getHourMinuteFormatFromLong(newPlanEntity.startTime),
                        newPlanEntity.startTime
                    )
                }
                if (isNotifyEndPlan && newPlanEntity.type == TYPE_PLAN) {
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
                    plan.startTime = if (plan.type == TYPE_PLAN) timeStart else 0
                    plan.endTime = if (plan.type == TYPE_PLAN) timeEnd else 0
                    plan.id = -i.toLong()
                    listPlan.add(plan)
                    if (isNotifyStartPlan && newPlanEntity.type == TYPE_PLAN) {
                        setReminder(
                            this,
                            plan.content,
                            DateUtil.getHourMinuteFormatFromLong(plan.startTime),
                            plan.startTime
                        )
                    }
                    if (isNotifyEndPlan && newPlanEntity.type == TYPE_PLAN) {
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
        showLoading()
        listPlan.forEach {
            if (it.name.isEmpty()) {
                it.name = "."
            }
        }
        apiService.syncPlan(email, listPlan).enqueueShort(success = {
            hideLoading()
            if (it.code() == 200) {
                showToast("Thêm thành công")
                finish()
            } else {
                showToast("${it.raw()}")
            }
        }, failed = {
            hideLoading()
            showToast("Lỗi khi thêm nhiệm vụ: ${it.message}")
        })
    }

    private fun showLoading() {
        binding.loading.show()
    }

    private fun hideLoading() {
        binding.loading.gone()
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

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            if (isValidateTime(hourOfDay, minute)) {
                binding.tvEndTime.text =
                    "Thời gian kết thúc: ${DateUtil.formatHourMinutes(hourOfDay, minute)}"
                lastSelectedHourEnd = hourOfDay
                lastSelectedMinuteEnd = minute
            } else {
                showToast("Thời gian kết thúc phải lớn hơn thời gian bắt đầu")
            }
        }
        val timePickerDialog = TimePickerDialog(
            this,
            R.style.DialogTheme,
            timeSetListener, lastSelectedHourEnd, lastSelectedMinuteEnd, is24HView
        )
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogTime() {
        val is24HView = true

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            binding.tvStartTime.text =
                "Thời gian bắt đầu: ${DateUtil.formatHourMinutes(hourOfDay, minute)}"
            lastSelectedHourStart = hourOfDay
            lastSelectedMinuteStart = minute
        }
        val timePickerDialog = TimePickerDialog(
            this,
            R.style.DialogTheme,
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

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityCopyPlanBinding {
        return ActivityCopyPlanBinding.inflate(inflater)
    }
}