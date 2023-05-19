package com.dd.company.dailyplanner.ui.home

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.databinding.LayoutDialogConfirmRemovePlanBinding
import com.dd.company.dailyplanner.utils.setOnSafeClick

class DialogConfirmRemovePlan(val context: Context) {
    private val binding by lazy {
        LayoutDialogConfirmRemovePlanBinding.inflate(LayoutInflater.from(context))
    }
    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(context, R.style.dialog_transparent_width).setView(binding.root)
            .create()
    }

    fun isShowing() = dialog.isShowing

    fun hide() {
        dialog.dismiss()
    }

    fun show(onConfirmRemovePlan: () -> Unit) {
        binding.btnRemovePlan.setOnSafeClick {
            hide()
            onConfirmRemovePlan.invoke()
        }
        binding.btnCancel.setOnSafeClick {
            hide()
        }
        if (!isShowing()) {
            dialog.show()
        }
    }
}