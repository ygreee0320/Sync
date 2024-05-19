package com.example.sync_front.chatting

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.R
import com.example.sync_front.api_server.ChattingRoom

class RoomAdapter(private var rooms: List<ChattingRoom>): RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newRooms: List<ChattingRoom>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.room_title)
        private val chattingTextView: TextView = itemView.findViewById(R.id.last_chatting)
        private val timeTextView: TextView = itemView.findViewById(R.id.last_time)
        private val totalTextView: TextView = itemView.findViewById(R.id.member_count)

        init {
            itemView.setOnClickListener {
                val clickedRoom = rooms[adapterPosition]
                val roomName = clickedRoom.roomName // 클릭된 Id를 가져옴
                val intent = Intent(itemView.context, ChattingActivity::class.java)
                intent.putExtra("roomName", roomName)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(roomList: ChattingRoom) {
            titleTextView.text = roomList.syncName
            totalTextView.text = roomList.total.toString()
            chattingTextView.text = roomList.lastText
            timeTextView.text = roomList.lastTime
        }

    }
}