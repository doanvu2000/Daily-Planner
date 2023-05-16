package com.dd.company.dailyplanner.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.data.WeekEntity
import com.dd.company.dailyplanner.databinding.ItemWeeklyHomeBinding
import com.dd.company.dailyplanner.ui.base.BaseAdapterRecyclerView
import com.dd.company.dailyplanner.utils.getDrawableIdByName
import com.dd.company.dailyplanner.utils.visibleByCondition

class WeekAdapter : BaseAdapterRecyclerView<WeekEntity, ItemWeeklyHomeBinding>() {
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemWeeklyHomeBinding {
        return ItemWeeklyHomeBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemWeeklyHomeBinding, item: WeekEntity, position: Int) {
        binding.apply {
            val colorText = if (item.isSelected) R.color.white else R.color.black
            tvDayNumber.apply {
                text = item.day.toString()
                setTextColor(ContextCompat.getColor(binding.root.context, colorText))
            }
            tvDayTitle.text = item.title
            imgCheckMission.apply {
                visibleByCondition(item.haveMission)
                setImageResource(binding.root.context.getDrawableIdByName(item.icon))
            }
            imgBackground.visibleByCondition(item.isSelected)
        }
    }

    fun setSelected(position: Int) {
        val indexSelected = dataList.indexOfFirst { it.isSelected }
        if (indexSelected != -1) {
            dataList[indexSelected].isSelected = false
            notifyItemChanged(indexSelected)
        }
        dataList[position].isSelected = true
        notifyItemChanged(position)
    }
}