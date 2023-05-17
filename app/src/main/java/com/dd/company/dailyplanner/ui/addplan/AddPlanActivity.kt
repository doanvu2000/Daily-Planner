package com.dd.company.dailyplanner.ui.addplan

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.view.LayoutInflater
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.databinding.ActivityAddPlanBinding
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.utils.*

class AddPlanActivity : BaseActivity<ActivityAddPlanBinding>() {
    companion object {
        const val COLOR_BLACK = "#000000"
        const val COLOR_TEXT_YEAR = "#59AAD7"
        const val LOOP_ONE = 1
        const val LOOP_DAY = 2
        const val LOOP_WEEK = 3
        const val LOOP_MONTH = 4
    }

    private var isSelectIcon = false
    private val dialogChooseIcon by lazy {
        DialogChooseIcon(this)
    }
    private val planEntity by lazy {
        PlanEntity()
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
                }
                hideAllBasePlan()
            }
        }
        binding.edtTitlePlan.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                showAllBasePlan()
                if (!isSelectIcon) {
                    binding.imgIconPlan.setImageResource(baseIcon[0])
                }
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
            binding.layoutSetTime.gone()
            binding.layoutInbox.show()
            binding.btnAddToBox.gone()
            binding.btnAddTime.show()
        }
        binding.btnAddTime.setOnClickListener {
            binding.layoutSetTime.show()
            binding.layoutInbox.gone()
            binding.btnAddToBox.show()
            binding.btnAddTime.gone()
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
    }

    private fun setUpViewLoop(loop: Int) {
        val resource = R.drawable.background_button_continue
        val colorWhite = ContextCompat.getColor(this,R.color.white)
        val colorLoop = ContextCompat.getColor(this,R.color.text_loop)

        binding.tvLoopOne.setBackgroundResource(if (loop == LOOP_ONE) resource else 0)
        binding.tvLoopDay.setBackgroundResource(if (loop == LOOP_DAY) resource else 0)
        binding.tvLoopWeek.setBackgroundResource(if (loop == LOOP_WEEK) resource else 0)
        binding.tvLoopMonth.setBackgroundResource(if (loop == LOOP_MONTH) resource else 0)

        binding.tvLoopOne.setTextColor(if (loop == LOOP_ONE) colorWhite else colorLoop)
        binding.tvLoopDay.setTextColor(if (loop == LOOP_DAY) colorWhite else colorLoop)
        binding.tvLoopWeek.setTextColor(if (loop == LOOP_WEEK) colorWhite else colorLoop)
        binding.tvLoopMonth.setTextColor(if (loop == LOOP_MONTH) colorWhite else colorLoop)
    }

    private var lastSelectedHourStart = 8
    private var lastSelectedMinuteStart = 0
    private var lastSelectedHourEnd = 8
    private var lastSelectedMinuteEnd = 0

    @SuppressLint("SetTextI18n")
    private fun showDialogTimeEnd() {
        val is24HView = true

        val timeSetListener = OnTimeSetListener { view, hourOfDay, minute ->
            if (isValidateTime(hourOfDay, minute)) {
                binding.tvEndTime.text = "Time end: ${DateUtil.formatHourMinutes(hourOfDay, minute)}"
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

        val timeSetListener = OnTimeSetListener { view, hourOfDay, minute ->
            binding.tvStartTime.text = "Time start: ${DateUtil.formatHourMinutes(hourOfDay, minute)}"
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
            dialogChooseIcon.show {
                binding.imgIconPlan.setImageResource(it)
                isSelectIcon = true
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