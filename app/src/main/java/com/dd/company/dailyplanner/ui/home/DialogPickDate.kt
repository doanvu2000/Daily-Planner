package com.dd.company.dailyplanner.ui.home

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.databinding.LayoutDialogPickDateAddBeanBinding
import com.dd.company.dailyplanner.utils.setOnSafeClick

class DialogPickDate(val context: Context) {
    private val binding by lazy {
        LayoutDialogPickDateAddBeanBinding.inflate(LayoutInflater.from(context))
    }
    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(context, R.style.dialog_transparent_width).setView(binding.root)
            .create()
    }

    init {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun hide() {
        dialog.dismiss()
    }

    fun show(
        currentDay: Int,
        currentMonth: Int,
        currentYear: Int,
        cancelAble: Boolean? = true,
        onClickSubmit: ((day: Int, month: Int, year: Int) -> Unit)? = null
    ) {
        binding.datePicker.updateDate(currentYear, currentMonth, currentDay)
        cancelAble?.let {
            dialog.setCancelable(it)
        }
        binding.btnCancel.setOnSafeClick {
            hide()
        }
        binding.btnClose.setOnSafeClick {
            hide()
        }
        binding.btnOk.setOnSafeClick {
            hide()
            val day = binding.datePicker.dayOfMonth
            val month = binding.datePicker.month
            val year = binding.datePicker.year
            onClickSubmit?.invoke(day, month, year)
        }
        if (!dialog.isShowing)
            dialog.show()
    }
}