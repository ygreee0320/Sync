package com.example.sync_front.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.api_server.Chatting
import com.example.sync_front.databinding.ActivityChattingBinding

class ChattingActivity : AppCompatActivity() {
    lateinit var binding: ActivityChattingBinding
    lateinit var myName : String
    lateinit var chattingList: MutableList<Chatting>
    private lateinit var adapter: ChattingAdapter
    lateinit var roomCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) //타이틀 없애기

        // 어댑터를 설정하고 리사이클러뷰에 연결
        chattingList = mutableListOf<Chatting>()
        adapter = ChattingAdapter(chattingList, myName)
        binding.chattingRecyclerview.layoutManager = LinearLayoutManager(this@ChattingActivity)
        binding.chattingRecyclerview.adapter = adapter

        // 메세지 전송 버튼 클릭 시
        binding.sendBtn.setOnClickListener {
            val message = binding.sendTxt.text.toString().trim()
            if (message.isEmpty()) {
                Toast.makeText(this@ChattingActivity, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else { // 메세지가 있다면 전송
                saveStore() //전송
                binding.sendTxt.setText("") // 텍스트창 초기화

                getStore() // 채팅창 업데이트
            }
        }
    }

    fun saveStore() {

    }

    fun getStore() {

    }

}