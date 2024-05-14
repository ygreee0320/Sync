package com.example.sync_front.ui.main.community

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.R
import com.example.sync_front.api_server.Community

class CommunityAdapter(private var list: List<Community>): RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_community, parent, false)
        return CommunityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newList: List<Community>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class CommunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profile: ImageView = itemView.findViewById(R.id.profile)
        private val name: TextView = itemView.findViewById(R.id.name)
        private val time: TextView = itemView.findViewById(R.id.time)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val content: TextView = itemView.findViewById(R.id.content)
        private val likeCount: TextView = itemView.findViewById(R.id.like_count)
        private val commentCount: TextView = itemView.findViewById(R.id.comment_count)

        init {
            itemView.setOnClickListener {
                val clickedItem = list[adapterPosition]
                val intent = Intent(itemView.context, SearchCommunityActivity::class.java)
                intent.putExtra("selectedId", clickedItem.postId) // 클릭된 Id
                itemView.context.startActivity(intent)
            }
        }

        fun bind(item: Community) {
            name.text = item.writerName
            time.text = item.createdData
            title.text = item.title
            content.text = item.content
            likeCount.text = item.likeCnt.toString()
            commentCount.text = item.commentCnt.toString()
        }
    }
}