package com.dd.company.dailyplanner.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.databinding.LayoutDialogPickStartValueBinding
import com.dd.company.dailyplanner.ui.setting.startday.PickOptionValueAdapter


fun Context.showDialogPickOptionValue(callbacks: (String, Int) -> Unit) {
    val binding = LayoutDialogPickStartValueBinding.inflate(LayoutInflater.from(this))
    val builder = AlertDialog.Builder(this).apply {
        setView(binding.root)
    }
    val dialog = builder.create()
    val data: List<String> = resources.getStringArray(R.array.day_of_week_value).toList()

    binding.tvTitleDialogPickStart.text = "Ngày bắt đầu của tuần"

    val pickerAdapter = PickOptionValueAdapter(this, data) { str, pos ->
        callbacks(str, pos)
        dialog.dismiss()
    }
    binding.rvDataPicker.adapter = pickerAdapter
    dialog.show()
}