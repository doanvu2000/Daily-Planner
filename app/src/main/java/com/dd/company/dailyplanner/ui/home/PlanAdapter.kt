package com.dd.company.dailyplanner.ui.home

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.databinding.ItemLayoutPlanBinding
import com.dd.company.dailyplanner.ui.base.BaseAdapterRecyclerView
import com.dd.company.dailyplanner.utils.DateUtil
import com.dd.company.dailyplanner.utils.getDrawableIdByName

class PlanAdapter : BaseAdapterRecyclerView<PlanEntity, ItemLayoutPlanBinding>() {
    var onClickCheckBox: ((item: PlanEntity) -> Unit)? = null
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemLayoutPlanBinding {
        return ItemLayoutPlanBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bindData(binding: ItemLayoutPlanBinding, item: PlanEntity, position: Int) {
        val context = binding.root.context
        binding.tvTimeStart.text = getHourMinutes(item.startTime)
        binding.tvTimeEnd.text = getHourMinutes(item.endTime)
        binding.imgIconPlan.setImageResource(context.getDrawableIdByName(item.icon))
        binding.tvTimeCountPlan.text = "${binding.tvTimeStart.text}-${binding.tvTimeEnd.text} " +
                "(${DateUtil.diffTime(item.startTime, item.endTime)})"
        binding.tvDescriptionPlan.text = item.content
        if (item.isDone) {
            binding.btnCheckbox.setImageResource(R.drawable.ic_checkbox_selected)
            binding.tvDescriptionPlan.paintFlags = binding.tvDescriptionPlan.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            binding.btnCheckbox.setImageResource(R.drawable.ic_checkbox)
            binding.tvDescriptionPlan.paintFlags = binding.tvTimeStart.paintFlags
        }
        binding.btnCheckbox.setOnClickListener {
            item.isDone = !item.isDone
            notifyItemChanged(position)
            onClickCheckBox?.invoke(item)
        }
    }

    private fun getHourMinutes(time: Long): String {
        val listTime = DateUtil.getTimeFormat(time, DateUtil.DATE_FORMAT_HOUR_MINUTES).split("-")
        return "${listTime[3]}:${listTime[4]}"
    }
}