package com.dd.company.dailyplanner.ui.inbox

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.databinding.LayoutItemInboxNotDoneBinding
import com.dd.company.dailyplanner.ui.base.BaseAdapterRecyclerView
import com.dd.company.dailyplanner.utils.DateUtil
import com.dd.company.dailyplanner.utils.getDrawableIdByName
import com.dd.company.dailyplanner.utils.gone
import com.dd.company.dailyplanner.utils.setOnSafeClick

class InboxDoneAdapter : BaseAdapterRecyclerView<PlanEntity, LayoutItemInboxNotDoneBinding>() {
    var onClickPlus: ((item: PlanEntity, position: Int) -> Unit)? = null
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): LayoutItemInboxNotDoneBinding {
        return LayoutItemInboxNotDoneBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: LayoutItemInboxNotDoneBinding, item: PlanEntity, position: Int) {
        binding.imgIconPlan.setImageResource(binding.root.context.getDrawableIdByName(item.icon))
        val diffTime = DateUtil.diffTime(item.startTime, item.endTime)
        binding.tvTimeCountPlan.text = diffTime.ifEmpty {
            binding.tvTimeCountPlan.gone()
            ""
        }
        binding.tvDescriptionPlan.apply {
            text = item.content
            paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        binding.btnPlus.setOnSafeClick {
            onClickPlus?.invoke(item, position)
            dataList.remove(item)
            notifyItemRemoved(position)
        }
    }
}