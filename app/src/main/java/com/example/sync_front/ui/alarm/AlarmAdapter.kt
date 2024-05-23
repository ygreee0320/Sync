package com.example.sync_front.ui.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.R
import com.example.sync_front.data.model.AlarmModel
import com.example.sync_front.databinding.ItemAlarmBinding

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {
    private var alarms: List<AlarmModel> = listOf()

    class AlarmViewHolder(private val binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: AlarmModel) {
            with(binding) {
                tvTitle.text = alarm.title
                tvContent.text = alarm.content
                tvDetailContent.text = alarm.detailContent
                tvCreatedDate.text = alarm.createdDate
                when (alarm.title) {
                    "커뮤니티" -> ivSyncImg.setImageResource(R.drawable.ic_alarm_community)
                    "채팅" -> ivSyncImg.setImageResource(R.drawable.ic_alarm_chat)
                    "공지" -> ivSyncImg.setImageResource(R.drawable.ic_alarm_notify)
                    "일정" -> ivSyncImg.setImageResource(R.drawable.ic_alarm_sync)
                    "후기" -> ivSyncImg.setImageResource(R.drawable.ic_alarm_sync)
                    else -> ivSyncImg.setImageResource(R.drawable.ic_alarm_sync)

                }
            }

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
