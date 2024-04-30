package com.example.sync_front.chatting

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.api_server.Chatting
import com.example.sync_front.databinding.ItemChattingMeBinding
import com.example.sync_front.databinding.ItemChattingOtherBinding

class MyChattingViewHolder(val binding: ItemChattingMeBinding) : RecyclerView.ViewHolder(binding.root)
class OtherChattingViewHolder(val binding: ItemChattingOtherBinding) : RecyclerView.ViewHolder(binding.root)

class ChattingAdapter(private val itemList: MutableList<Chatting>, private val myName: String? = null):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (viewType == 0) {
            return MyChattingViewHolder(ItemChattingMeBinding.inflate(layoutInflater))
        } else {
            return OtherChattingViewHolder(ItemChattingOtherBinding.inflate(layoutInflater))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].user == myName) {
            0 // 로그인한 사용자의 이름과 일치하는 경우 (내 채팅)
        } else {
            1 // 로그인한 사용자의 이름과 다른 경우 (다른 사람 채팅)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        val data = itemList.get(position)

        if (holder is MyChattingViewHolder) {
            holder.binding.run {
                chattingMessage.text = data.text
                messageTime.text = formatTime("${data.time}")
                chattingMessage.gravity = Gravity.RIGHT
            }
        } else if (holder is OtherChattingViewHolder) {
            holder.binding.run {
                chattingMessage.gravity = Gravity.LEFT
                chattingUser.text = "${data.user}"
                chattingMessage.text = data.text
                messageTime.text = data.time
            }
        }
    }

    private fun formatTime(dateTime: String): String {
        val dateTimeParts = dateTime.split(" ")
        if (dateTimeParts.size >= 2) {
            val timeParts = dateTimeParts[1].split(":")
            if (timeParts.size >= 2) {
                val hour = timeParts[0]
                val minute = timeParts[1]
                return "$hour:$minute"
            }
        }
        return ""
    }

    fun setData(list: List<Chatting>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }
}