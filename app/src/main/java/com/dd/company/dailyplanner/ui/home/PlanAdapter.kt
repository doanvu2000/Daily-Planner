package com.dd.company.dailyplanner.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.databinding.ItemLayoutPlanBinding
import com.dd.company.dailyplanner.ui.base.BaseAdapterRecyclerView
import com.dd.company.dailyplanner.utils.DateUtil
import com.dd.company.dailyplanner.utils.getDrawableIdByName

class PlanAdapter : BaseAdapterRecyclerView<PlanEntity, ItemLayoutPlanBinding>() {
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
    }

    private fun getHourMinutes(time: Long): String {
        val listTime = DateUtil.getTimeFormat(time, DateUtil.DATE_FORMAT_HOUR_MINUTES).split("-")
        return "${listTime[3]}:${listTime[4]}"
    }
}