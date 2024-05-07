package com.example.sync_front.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.R
import android.util.Log
import com.example.sync_front.databinding.ItemSyncBinding
import org.w3c.dom.Text

data class Event(
    val label1: String,
    val label2: String,
    val participantCount: Int,
    val totalMembers: Int,
    val title: String,
    val rotation: String,
    val date: String,
    val imageRes: Int
)

class EventAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemSyncBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        with(holder.binding) {
            ivSyncImg.setImageResource(event.imageRes)
            syncLabel1.text = event.label1
            syncLabel2.text = event.label2
            tvSyncTitle.text = event.title
            syncNumberOfGather.text = event.participantCount.toString()
            syncNumberOfTotal.text = event.totalMembers.toString()
            tvSyncRotation.text = event.rotation
            tvSyncCalendar.text = event.date
        }
        Log.d("RecyclerView", "Binding position $position with title ${event.title}")
    }

    override fun getItemCount() = events.size

    inner class EventViewHolder(val binding: ItemSyncBinding) :
        RecyclerView.ViewHolder(binding.root)
}
