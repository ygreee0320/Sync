package com.example.sync_front.chatting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.api_server.ChattingRoom
import com.example.sync_front.databinding.FragmentChattingBinding

class ChattingFragment : Fragment() {
    lateinit var binding: FragmentChattingBinding
    lateinit var roomList: List<ChattingRoom>
    private lateinit var adapter: RoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(inflater, container, false)

        // 어댑터를 설정하고 리사이클러뷰에 연결
        adapter = RoomAdapter(roomList)
        binding.roomRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.roomRecyclerview.adapter = adapter

        return binding.root
    }

}