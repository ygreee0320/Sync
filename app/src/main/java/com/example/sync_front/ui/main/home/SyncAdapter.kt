package com.example.sync_front.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.bumptech.glide.Glide
import com.example.sync_front.R
import com.example.sync_front.data.model.Sync
import com.example.sync_front.databinding.ItemAssociateBinding
import com.example.sync_front.databinding.ItemSyncBinding


class SyncAdapter(
    private var syncs: List<Sync>,
    private val onSyncClickListener: OnSyncClickListener
) :
    RecyclerView.Adapter<SyncAdapter.SyncViewHolder>() {

    interface OnSyncClickListener {
        fun onSyncClick(sync: Sync)
    }

    class SyncViewHolder(val binding: ItemSyncBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sync: Sync, onSyncClickListener: OnSyncClickListener) {
            with(binding) {
                Glide.with(ivSyncImg.context)
                    .load(sync.image)
                    .placeholder(R.drawable.img_sample_gathering)
                    .error(R.drawable.img_sample_gathering)
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
                itemView.setOnClickListener {
                    onSyncClickListener.onSyncClick(sync)
                }
                if (sync.isMarked) {
                    syncIcBookmark.setBackgroundResource(R.drawable.ic_bookmark_yes)
                } else {
                    syncIcBookmark.setBackgroundResource(R.drawable.ic_bookmark_no)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyncViewHolder {
        val binding = ItemSyncBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SyncViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SyncViewHolder, position: Int) {
        holder.bind(syncs[position], onSyncClickListener)
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

class AssociateSyncAdapter(
    private var associateSyncs: List<Sync>,
    private val onSyncClickListener: SyncAdapter.OnSyncClickListener
) :
    RecyclerView.Adapter<AssociateSyncAdapter.AssociateViewHolder>() {
    interface OnSyncClickListener {
        fun onSyncClick(sync: Sync)
    }

    class AssociateViewHolder(val binding: ItemAssociateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sync: Sync, onSyncClickListener: SyncAdapter.OnSyncClickListener) {
            with(binding) {
                Glide.with(ivSyncImg.context)
                    .load(sync.image)
                    .placeholder(R.drawable.img_sample_gathering)
                    .error(R.drawable.img_sample_gathering)
                    .into(ivSyncImg)
                syncLabel1.text = sync.syncType
                syncLabel2.text = sync.type
                tvSyncAssociate.text = sync.associate
                tvSyncTitle.text = sync.syncName
                tvSyncLocation.text = sync.location
                tvSyncCalendar.text = sync.date
                syncNumberOfGather.text = sync.userCnt.toString()
                syncNumberOfTotal.text = sync.totalCnt.toString()
                itemView.setOnClickListener {
                    onSyncClickListener.onSyncClick(sync)
                }
                tvSyncTitle.isSelected = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssociateViewHolder {
        val binding =
            ItemAssociateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AssociateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AssociateViewHolder, position: Int) {
        holder.bind(associateSyncs[position], onSyncClickListener)
    }

    override fun getItemCount(): Int = associateSyncs.size

    fun updateSyncs(newSyncs: List<Sync>) {
        this.associateSyncs = newSyncs
        notifyDataSetChanged()
    }
}
