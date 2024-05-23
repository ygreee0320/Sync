package com.example.sync_front.ui.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.data.model.AlarmModel
import com.example.sync_front.databinding.ItemAlarmBinding

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {
    private var alarms: List<AlarmModel> = listOf()

    class AlarmViewHolder(private val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: AlarmModel) {
            binding.tvTitle.text = alarm.title
            binding.tvContent.text = alarm.content
            binding.tvDetailContent.text = alarm.detailContent
            binding.tvCreatedDate.text = alarm.createdDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(alarms[position])
    }

    override fun getItemCount() = alarms.size

    fun setAlarms(alarms: List<AlarmModel>) {
        this.alarms = alarms
        notifyDataSetChanged()
    }
}
