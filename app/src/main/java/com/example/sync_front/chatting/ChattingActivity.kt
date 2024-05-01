package com.example.sync_front.chatting

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sync_front.api_server.ChatMessageRequestDto
import com.example.sync_front.api_server.Chatting
import com.example.sync_front.databinding.ActivityChattingBinding
import com.google.gson.Gson
import okhttp3.WebSocket
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompCommand
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket

class ChattingActivity : AppCompatActivity() {
    lateinit var binding: ActivityChattingBinding
    lateinit var myName : String
    lateinit var chattingList: MutableList<Chatting>
    private lateinit var adapter: ChattingAdapter
    lateinit var roomCode: String
    lateinit var webSocket: WebSocket
    private lateinit var mHandler: Handler
    private lateinit var serverAddr: InetAddress
    private lateinit var socket: Socket
    private lateinit var sendWriter: PrintWriter

    private lateinit var stompClient: StompClient


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) //타이틀 없애기

        // 어댑터를 설정하고 리사이클러뷰에 연결
        chattingList = mutableListOf<Chatting>()
//        adapter = ChattingAdapter(chattingList, myName)
//        binding.chattingRecyclerview.layoutManager = LinearLayoutManager(this@ChattingActivity)
//        binding.chattingRecyclerview.adapter = adapter


        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://kusitms28.store/ws")
        val headerList = arrayListOf<StompHeader>()

        stompClient.lifecycle().subscribe { lifecycleEvent: LifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> Log.d(TAG, "Stomp connection opened")
                LifecycleEvent.Type.ERROR -> {
                    Log.e(TAG, "Error", lifecycleEvent.exception)
                    if (lifecycleEvent.exception.message!!.contains("EOF")) {
                        //isUnexpectedClosed = true
                    }
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.d(TAG, "Stomp connection closed")
                }
                else -> {}
            }
        }
        stompClient.connect()

        // 메세지 전송 버튼 클릭 시
        binding.sendBtn.setOnClickListener {
            val message = binding.sendTxt.text.toString().trim()
            if (message.isEmpty()) {
                Toast.makeText(this@ChattingActivity, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else { // 메세지가 있다면 전송
                //saveStore() //전송

                if (stompClient.isConnected()) {
                    Log.d("WebSocket", "연결됨")
                } else {
                    Log.d("WebSocket", "연결되지 않음")
                }

                binding.sendTxt.setText("") // 텍스트창 초기화

                val chatMessage = ChatMessageRequestDto(
                    "116676399746926313917_bedce5",
                    "배현서",
                    "배횬서",
                    message
                )
                val sessionId = "100433582158242230829_33f8bc" // 실제 세션 ID로 대체해야 합니다.
                val json = Gson().toJson(chatMessage)
                Log.d("my log","보내는 정보 - ${json}")
                val headers = mapOf("sessionId" to sessionId).map { (key, value) ->
                    StompHeader(key, value)
                }.toList()

                stompClient.send("/pub/chat/${sessionId}", json).subscribe()

                //getStore() // 채팅창 업데이트
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stompClient.disconnect()
    }

}