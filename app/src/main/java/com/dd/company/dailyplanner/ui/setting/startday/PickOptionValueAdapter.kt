package com.dd.company.dailyplanner.ui.setting.startday

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dd.company.dailyplanner.databinding.ItemPickStartValueBinding
import com.dd.company.dailyplanner.utils.setOnSafeClick

class PickOptionValueAdapter(
    var context: Context,
    var data: List<String>,
    var onItemClick: (String, Int) -> Unit
) :
    RecyclerView.Adapter<PickOptionValueAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemPickStartValueBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPickStartValueBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = ViewHolder(binding)
        initOnclickListener(holder)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvPickerValue.text = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun initOnclickListener(holder: ViewHolder) {
        holder.itemView.setOnSafeClick {
            onItemClick.invoke(data[holder.layoutPosition], holder.layoutPosition)
        }
    }
}