package com.example.sync_front.ui.main.community

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sync_front.R
import com.example.sync_front.data.model.Community

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
        private val firstImage: ImageView = itemView.findViewById(R.id.main_img)

        init {
            itemView.setOnClickListener {
                val clickedItem = list[adapterPosition]
                val intent = Intent(itemView.context, CommunityDetailActivity::class.java)
                intent.putExtra("communityId", clickedItem.postId) // 클릭된 Id
                itemView.context.startActivity(intent)
            }
        }

        fun bind(item: Community) {
            name.text = item.writerName
            time.text = item.createdDate ?: "Unknown"
            title.text = item.title
            content.text = item.content
            likeCount.text = item.likeCnt.toString()
            commentCount.text = item.commentCnt.toString()

            if (!item.representativeImage.isNullOrEmpty()) {
                firstImage.visibility = View.VISIBLE

                Glide.with(itemView.context)
                    .load(item.representativeImage)
                    .into(firstImage)
            } else {
                // 대표 이미지 없으면 숨김
                firstImage.visibility = View.GONE
            }

            if (!item.writerImage.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(item.writerImage)
                    .placeholder(R.drawable.img_profile_default)
                    .error(R.drawable.img_profile_default)
                    .into(profile)
            } else {
                profile.setImageResource(R.drawable.img_profile_default)
            }
        }
    }
}