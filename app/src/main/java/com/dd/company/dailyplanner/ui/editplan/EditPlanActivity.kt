package com.dd.company.dailyplanner.ui.editplan

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
import com.dd.company.dailyplanner.databinding.ActivityEditPlanBinding
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

class EditPlanActivity : BaseActivity<ActivityEditPlanBinding>() {

    companion object {
        const val COLOR_BLACK = "#000000"
        const val COLOR_TEXT_YEAR = "#59AAD7"
        const val LOOP_ONE = 1
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
    lateinit var planEntity: PlanEntity
    private var isInbox = false
    private var listPlan: MutableList<PlanEntity> = mutableListOf()

    override fun initView() {
        val str1 = "Chỉnh sửa"
        val str2 = " nhiệm vụ"
        val text =
            "<font color=\"$COLOR_BLACK\">$str1</font><font color=\"$COLOR_TEXT_YEAR\">$str2</font>"
        binding.imgTitle.setTextHtml(text)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        planEntity = (intent?.extras?.getSerializable("plan_entity") as? PlanEntity) ?: PlanEntity()
        binding.tvStartTime.text =
            "Time start: ${DateUtil.getHourMinuteFormatFromLong(planEntity.startTime)}"
        binding.tvEndTime.text =
            "Time end: ${DateUtil.getHourMinuteFormatFromLong(planEntity.endTime)}"
        binding.imgIconPlan.setImageResource(getDrawableIdByName(planEntity.icon))
        lastSelectedHourStart = planEntity.startTime.getHour()
        lastSelectedMinuteStart = planEntity.startTime.getMinutes()
        lastSelectedHourEnd = planEntity.endTime.getHour()
        lastSelectedMinuteEnd = planEntity.endTime.getMinutes()
        year = planEntity.startTime.getYear()
        month = planEntity.startTime.getMonth()
        day = planEntity.startTime.getDay()
        imagePlan = planEntity.icon
        isInbox = intent?.extras?.getBoolean("is_inbox") ?: false
        if (isInbox) {
            planEntity.type = TYPE_INBOX
            binding.tvTitleLoop.gone()
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
        binding.edtTitlePlan.setText(planEntity.content)
        binding.edtDetailPlan.setText(planEntity.name)
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
            binding.tvTitleLoop.gone()
            binding.layoutLoop.gone()
            planEntity.type = TYPE_INBOX
            binding.layoutSetTime.gone()
            binding.layoutInbox.show()
            binding.btnAddToBox.gone()
            binding.btnAddTime.show()
            binding.tvNeedNotification.gone()
            binding.layoutNotification.gone()
        }
        binding.btnAddTime.setOnClickListener {
            binding.tvTitleLoop.show()
            binding.layoutLoop.show()
            planEntity.type = TYPE_PLAN
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
        planEntity.icon = imagePlan
        //content plan
        val title = binding.edtTitlePlan.text.toString()
        if (title.isEmpty()) {
            showToast(msg)
            return
        }
        planEntity.content = title
        //loop type: 1-> create 1, 2 -> create all month, 3 -> create 1 in week, 4-> create 1 in month
        //time start
        if (planEntity.type == TYPE_PLAN) {//if plan, save time start, end
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
            planEntity.apply {
                this.startTime = timeStart
                this.endTime = timeEnd
            }
        } else {
            planEntity.apply {
                this.startTime = 0
                this.endTime = 0
            }
        }
        //detail description
        planEntity.name = binding.edtDetailPlan.text.toString()
        val index = listPlan.indexOfFirst { it.id == planEntity.id }
        if (index != -1) {
            listPlan[index] = planEntity
        }
        if (isNotifyStartPlan && planEntity.type == TYPE_PLAN) {
            setReminder(
                this,
                planEntity.content,
                DateUtil.getHourMinuteFormatFromLong(planEntity.startTime),
                planEntity.startTime
            )
        }
        if (isNotifyEndPlan && planEntity.type == TYPE_PLAN) {
            setReminder(
                this,
                planEntity.content,
                DateUtil.getHourMinuteFormatFromLong(planEntity.endTime),
                planEntity.endTime
            )
        }
        showLoading()
        apiService.syncPlan(email, listPlan).enqueueShort(success = {
            hideLoading()
            showToast("edit plan success")
            finish()
        }, failed = {
            hideLoading()
            showToast("Error when add plan: ${it.message}")
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
        binding.tvLoopWeek.setBackgroundResource(if (loop == LOOP_WEEK) resource else 0)
        binding.tvLoopMonth.setBackgroundResource(if (loop == LOOP_MONTH) resource else 0)

        binding.tvLoopOne.setTextColor(if (loop == LOOP_ONE) colorWhite else colorLoop)
        binding.tvLoopWeek.setTextColor(if (loop == LOOP_WEEK) colorWhite else colorLoop)
        binding.tvLoopMonth.setTextColor(if (loop == LOOP_MONTH) colorWhite else colorLoop)
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogTimeEnd() {
        val is24HView = true

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
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

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
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

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityEditPlanBinding {
        return ActivityEditPlanBinding.inflate(inflater)
    }
}