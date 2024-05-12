package com.example.sync_front.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.bumptech.glide.Glide
import com.example.sync_front.data.model.Sync
import com.example.sync_front.databinding.ItemSyncBinding

data class Event(
    val label1: String,
    val label2: String,
    val participantCount: Int,
    val totalMembers: Int,
    val title: String,
    val location: String,
    val date: String,
    val imageRes: Int
)

class SyncAdapter(private var syncs: List<Sync>) :
    RecyclerView.Adapter<SyncAdapter.SyncViewHolder>() {

    class SyncViewHolder(val binding: ItemSyncBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sync: Sync) {
            with(binding) {
                Glide.with(ivSyncImg.context)
                    .load(sync.image)
                    .into(ivSyncImg)
                syncLabel1.text = sync.syncType
                syncLabel2.text = sync.type
                tvSyncTitle.text = sync.syncName
                syncNumberOfGather.text = sync.userCnt.toString()
                syncNumberOfTotal.text = sync.totalCnt.toString()
                tvSyncLocation.text = sync.location
                tvSyncCalendar.text = sync.date
                syncIcBookmark.setOnClickListener {
                    it.isSelected = !it.isSelected
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyncViewHolder {
        val binding = ItemSyncBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SyncViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SyncViewHolder, position: Int) {
        holder.bind(syncs[position])
        Log.d("SyncAdapter1", "Binding view holder for position $position")
    }

    override fun getItemCount(): Int {
        Log.d("SyncAdapter2", "Item count: ${syncs.size}")
        return syncs.size
    }

    fun updateSyncs(newSyncs: List<Sync>) {
        this.syncs = newSyncs
        notifyDataSetChanged()
    }
}

