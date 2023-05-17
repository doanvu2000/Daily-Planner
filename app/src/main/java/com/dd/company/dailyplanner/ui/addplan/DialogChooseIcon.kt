package com.dd.company.dailyplanner.ui.addplan

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.databinding.LayoutDialogChooseIconBinding
import com.dd.company.dailyplanner.utils.setGridManager

class DialogChooseIcon(val context: Context) {
    private val binding by lazy {
        LayoutDialogChooseIconBinding.inflate(LayoutInflater.from(context))
    }

    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(context, R.style.dialog_transparent_width).setView(binding.root)
            .create()
    }

    private val chooseIconAdapter by lazy {
        ChooseIconAdapter()
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun hide() {
        dialog.dismiss()
    }

    fun show(
        isCancelAble: Boolean? = true,
        onClickSubmit: ((icon: Int) -> Unit)? = null
    ) {
        setUpListIcon()
        isCancelAble?.let {
            dialog.setCancelable(it)
        }
        chooseIconAdapter.setOnClickItem { item, position ->
            hide()
            onClickSubmit?.invoke(item ?: R.drawable.ic_email)
        }
        if (!isShowing()) {
            dialog.show()
        }
    }

    private fun setUpListIcon() {
        binding.rcvIcon.setGridManager(context, 6, chooseIconAdapter)
        chooseIconAdapter.setDataList(
            getListIcon()
        )
    }

    private fun getListIcon() = mutableListOf(
        R.drawable.ic_email,
        R.drawable.ic_running,
        R.drawable.ic_watch_tv,
        R.drawable.ic_wake,
        R.drawable.ic_email,
        R.drawable.ic_running,
        R.drawable.ic_watch_tv,
        R.drawable.ic_wake,
        R.drawable.ic_email,
        R.drawable.ic_running,
        R.drawable.ic_watch_tv,
        R.drawable.ic_wake,
        R.drawable.ic_email,
        R.drawable.ic_running,
        R.drawable.ic_watch_tv,
        R.drawable.ic_wake,
        R.drawable.ic_email,
        R.drawable.ic_running,
        R.drawable.ic_watch_tv,
        R.drawable.ic_wake, R.drawable.ic_email,
        R.drawable.ic_running,
        R.drawable.ic_watch_tv,
        R.drawable.ic_wake,
        R.drawable.ic_email,
        R.drawable.ic_running,
        R.drawable.ic_watch_tv,
        R.drawable.ic_wake
    )
}