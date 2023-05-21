package com.dd.company.dailyplanner.ui.addplan

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.data.IconEntity
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
        onClickSubmit: ((icon: Int, name: String) -> Unit)? = null
    ) {
        setUpListIcon()
        isCancelAble?.let {
            dialog.setCancelable(it)
        }
        chooseIconAdapter.setOnClickItem { item, position ->
            hide()
            onClickSubmit?.invoke(item?.icon ?: R.drawable.ic_email, item?.name ?: "ic_email")
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
        IconEntity("ic_email", R.drawable.ic_email),
        IconEntity("ic_running", R.drawable.ic_running),
        IconEntity("ic_watch_tv", R.drawable.ic_watch_tv),
        IconEntity("ic_wake", R.drawable.ic_wake),
        IconEntity("icon_1", R.drawable.icon_1),
        IconEntity("icon_2", R.drawable.icon_2),
        IconEntity("icon_3", R.drawable.icon_3),
        IconEntity("icon_4", R.drawable.icon_4),
        IconEntity("icon_5", R.drawable.icon_5),
        IconEntity("icon_6", R.drawable.icon_6),
        IconEntity("icon_7", R.drawable.icon_7),
        IconEntity("icon_8", R.drawable.icon_8),
        IconEntity("icon_9", R.drawable.icon_9),
        IconEntity("icon_10", R.drawable.icon_10),
        IconEntity("icon_11", R.drawable.icon_11),
        IconEntity("icon_12", R.drawable.icon_12),
        IconEntity("icon_13", R.drawable.icon_13),
        IconEntity("icon_14", R.drawable.icon_14),
        IconEntity("icon_15", R.drawable.icon_15),
        IconEntity("icon_16", R.drawable.icon_16),
        IconEntity("icon_17", R.drawable.icon_17),
        IconEntity("icon_18", R.drawable.icon_18),
        IconEntity("icon_19", R.drawable.icon_19),
        IconEntity("icon_20", R.drawable.icon_20),
        IconEntity("icon_21", R.drawable.icon_21),
        IconEntity("icon_22", R.drawable.icon_22),
        IconEntity("icon_23", R.drawable.icon_23),
        IconEntity("icon_24", R.drawable.icon_24),
        IconEntity("icon_25", R.drawable.icon_25),
        IconEntity("icon_26", R.drawable.icon_26),
        IconEntity("icon_27", R.drawable.icon_27),
        IconEntity("icon_28", R.drawable.icon_28),
        IconEntity("icon_29", R.drawable.icon_29),
        IconEntity("icon_30", R.drawable.icon_30),
        IconEntity("icon_31", R.drawable.icon_31),
        IconEntity("icon_32", R.drawable.icon_32),
        IconEntity("icon_33", R.drawable.icon_33),
        IconEntity("icon_34", R.drawable.icon_34),
        IconEntity("icon_35", R.drawable.icon_35),
        IconEntity("icon_36", R.drawable.icon_36),
        IconEntity("icon_37", R.drawable.icon_37),
        IconEntity("icon_38", R.drawable.icon_38),
        IconEntity("icon_39", R.drawable.icon_39),
        IconEntity("icon_40", R.drawable.icon_40),
        IconEntity("icon_41", R.drawable.icon_41),
        IconEntity("icon_42", R.drawable.icon_42),
        IconEntity("icon_43", R.drawable.icon_43),
        IconEntity("icon_44", R.drawable.icon_44),
        IconEntity("icon_45", R.drawable.icon_45),
        IconEntity("icon_46", R.drawable.icon_46),
        IconEntity("icon_47", R.drawable.icon_47),
        IconEntity("icon_48", R.drawable.icon_48),
        IconEntity("icon_49", R.drawable.icon_49),
        IconEntity("icon_50", R.drawable.icon_50),
        IconEntity("icon_51", R.drawable.icon_51),
        IconEntity("icon_52", R.drawable.icon_52),
        IconEntity("icon_53", R.drawable.icon_53),
        IconEntity("icon_54", R.drawable.icon_54)
    )
}