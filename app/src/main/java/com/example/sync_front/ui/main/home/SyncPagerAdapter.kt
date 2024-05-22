import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sync_front.R
import com.example.sync_front.data.model.Sync
import com.example.sync_front.databinding.ItemViewPagerBinding
import com.example.sync_front.ui.main.home.SyncAdapter

class SyncPagerAdapter(
    private val syncList: List<Sync>,
    private val onSyncClickListener: SyncAdapter.OnSyncClickListener
) :
    RecyclerView.Adapter<SyncPagerAdapter.SyncViewHolder>() {
    interface OnSyncClickListener {
        fun onSyncClick(sync: Sync)
    }

    class SyncViewHolder(val binding: ItemViewPagerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sync: Sync, onSyncClickListener: SyncAdapter.OnSyncClickListener, position: Int, totalCount: Int) {
            with(binding) {
                Glide.with(ivSyncImg.context)
                    .load(sync.image)
                    .placeholder(R.drawable.img_sample_gathering)
                    .error(R.drawable.img_sample_gathering)
                    .into(ivSyncImg)
                syncLabel1.text = sync.syncType
                syncLabel2.text = sync.type
                syncLabel3.text = "${position + 1}/$totalCount" // 현재 페이지 숫자/전체 페이지 숫자
                syncNumberOfGather.text = sync.userCnt.toString()
                syncNumberOfTotal.text = sync.totalCnt.toString()
                tvSyncTitle.text = sync.syncName
                tvSyncLocation.text = sync.location
                tvSyncCalendar.text = sync.date

                itemView.setOnClickListener {
                    onSyncClickListener.onSyncClick(sync)
                }
            }
            Log.d("SyncPagerAdapter", "Binding data for position $position")
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyncViewHolder {
        val binding = ItemViewPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SyncViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SyncViewHolder, position: Int) {
        holder.bind(syncList[position], onSyncClickListener, position, itemCount)
    }

    override fun getItemCount(): Int {
        Log.d("SyncPagerAdapter", "getItemCount called: ${syncList.size}")
        return syncList.size
    }
}
