package com.dd.company.dailyplanner.ui.addplan

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dd.company.dailyplanner.databinding.ItemChooseIconBinding
import com.dd.company.dailyplanner.ui.base.BaseAdapterRecyclerView

class ChooseIconAdapter : BaseAdapterRecyclerView<Int, ItemChooseIconBinding>() {
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemChooseIconBinding {
        return ItemChooseIconBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemChooseIconBinding, item: Int, position: Int) {
        binding.imgIcon.setImageResource(item)
    }
}