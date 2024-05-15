import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.data.model.Sync
import com.example.sync_front.databinding.ItemViewPagerBinding

class SyncPagerAdapter(private val syncList: List<Sync>) :
    RecyclerView.Adapter<SyncPagerAdapter.SyncViewHolder>() {

    class SyncViewHolder(val binding: ItemViewPagerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sync: Sync) {
            with(binding) {
                syncLabel1.text = sync.syncType
                syncLabel2.text = sync.type
                syncLabel3.text = "1/7" // 예시
                syncNumberOfGather.text = sync.userCnt.toString()
                syncNumberOfTotal.text = sync.totalCnt.toString()
                tvSyncTitle.text = sync.syncName
                tvSyncLocation.text = sync.location
                tvSyncCalendar.text = sync.date
            }
            Log.d("SyncPagerAdapter", "Binding data for position $adapterPosition")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyncViewHolder {
        Log.d("SyncPagerAdapter", "onCreateViewHolder called")
        val binding = ItemViewPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SyncViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SyncViewHolder, position: Int) {
        Log.d("SyncPagerAdapter", "onBindViewHolder called for position $position")
        holder.bind(syncList[position])
    }

    override fun getItemCount(): Int {
        Log.d("SyncPagerAdapter", "getItemCount called: ${syncList.size}")
        return syncList.size
    }
}
