package com.example.sync_front.chatting

import android.content.Context
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
    lateinit var roomList: MutableList<ChattingRoom>
    private lateinit var adapter: RoomAdapter
    private var authToken: String ?= null // 로그인 토큰

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialSetting()
    }

    private fun initialSetting() {
        // 저장된 토큰 읽어오기
        val sharedPreferences = requireActivity().getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        // 어댑터를 설정하고 리사이클러뷰에 연결
        roomList = mutableListOf<ChattingRoom>()
        adapter = RoomAdapter(roomList)
        binding.roomRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.roomRecyclerview.adapter = adapter

        loadChattingRoom()
    }

    private fun loadChattingRoom() { // 내 채팅 룸 불러오기

    }

}