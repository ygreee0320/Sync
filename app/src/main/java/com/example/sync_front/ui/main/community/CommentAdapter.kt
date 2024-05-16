package com.example.sync_front.ui.main.community

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sync_front.R
import com.example.sync_front.api_server.Comment
import com.example.sync_front.api_server.CommunityManager

class CommentAdapter(private var list: List<Comment>,
                     private val authToken: String): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newList: List<Comment>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profile: ImageView = itemView.findViewById(R.id.profile)
        private val name: TextView = itemView.findViewById(R.id.name)
        private val time: TextView = itemView.findViewById(R.id.time)
        private val content: TextView = itemView.findViewById(R.id.content)
        private val likeCount: TextView = itemView.findViewById(R.id.like_count)
        private val likeBtn: LinearLayout = itemView.findViewById(R.id.like_btn)
        private var likedByUser: Boolean = false
        private var likeCnt: Int ?= 0

        init {
            likeBtn.setOnClickListener {
                val clicked = list[adapterPosition].commentId

                if (!likedByUser) {
                    CommunityManager.postCommentLike(authToken, clicked)
                } else {
                    CommunityManager.deleteCommentLike(authToken, clicked)
                }
            }
        }

        fun bind(item: Comment) {
            name.text = item.writerName
            time.text = item.createdDate
            content.text = item.content
            likeCount.text = item.likeCnt.toString()

            if (!item.writerImage.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(item.writerImage)
                    .into(profile)
            }
        }
    }
}