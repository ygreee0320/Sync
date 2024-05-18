package com.example.sync_front.chatting

import ChattingAdapter
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.api_server.*
import com.example.sync_front.databinding.ActivityChattingBinding
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.util.concurrent.atomic.AtomicBoolean

class ChattingActivity : AppCompatActivity() {
    lateinit var binding: ActivityChattingBinding
    lateinit var myName : String
    lateinit var chattingList: MutableList<RoomMessageElementResponseDto>
    private lateinit var adapter: ChattingAdapter
    private var authToken: String ?= null // 로그인 토큰

    private lateinit var stompClient: StompClient
    private val compositeDisposable = CompositeDisposable()
    val isUnexpectedClosed = AtomicBoolean(false)
    private lateinit var roomName: String

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayShowTitleEnabled(false) //타이틀 없애기

        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        myName = sharedPreferences.getString("name", "익명")!!

        initialSetting()

        roomName = "eksxhr"  //채팅방 이름

        // 어댑터를 설정하고 리사이클러뷰에 연결
        chattingList = mutableListOf<RoomMessageElementResponseDto>()
        adapter = ChattingAdapter(chattingList, myName)
        binding.chattingRecyclerview.layoutManager = LinearLayoutManager(this@ChattingActivity)
        binding.chattingRecyclerview.adapter = adapter

        //val headerList = arrayListOf<StompHeader>()
        initStomp()

        // 메세지 전송 버튼 클릭 시
        binding.sendBtn.setOnClickListener {
            val message = binding.sendTxt.text.toString().trim()

            if (message.isEmpty()) {
                Toast.makeText(this@ChattingActivity, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else { // 메세지가 있다면 전송
                if (stompClient.isConnected()) {
                    Log.d("WebSocket", "연결됨")
                    sendMessage(message) // 전송
                } else {
                    Log.d("WebSocket", "연결되지 않음")
                }
            }
        }

        // 키보드가 활성화되면 리사이클러뷰의 크기 조정
        binding.chattingRecyclerview.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            binding.chattingRecyclerview.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun initialSetting() {
        // 저장된 토큰 읽어오기
        val sharedPreferences = this.getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)
    }

    @SuppressLint("CheckResult")
    private fun initStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://kusitms28.store/ws")

        stompClient.lifecycle().subscribe { lifecycleEvent: LifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d(TAG, "Stomp connection opened")
                    requestAndSubscribeToChatDetails(roomName)
                }
                LifecycleEvent.Type.ERROR -> {
                    Log.e(TAG, "Error", lifecycleEvent.exception)
                    if (lifecycleEvent.exception.message!!.contains("EOF")) {
                        isUnexpectedClosed.set(true)
                    }
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.d(TAG, "Stomp connection closed")
                    if (isUnexpectedClosed.get()) {
                        initStomp()
                        isUnexpectedClosed.set(false)
                    }
                }
                else -> {}
            }
        }
        stompClient.connect()
    }

    private fun sendMessage(message : String) {
        binding.sendTxt.setText("") // 텍스트창 초기화

        val chatMessage = ChatMessageRequestDto(
            "100433582158242230829_334837",
            "배현서",
            roomName,
            message
        )
        val json = Gson().toJson(chatMessage)
        Log.d("my log","보내는 정보 - ${json}")

        stompClient.send("/pub/room/${roomName}", json).subscribe()
    }

    @SuppressLint("CheckResult")
    private fun requestAndSubscribeToChatDetails(roomName: String) {
        val topic = "/sub/room/$roomName"
        compositeDisposable.add(stompClient.topic(topic)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ stompMessage ->
                Log.d("my log", "${stompMessage}")

                val messageType = JSONObject(stompMessage.payload).getString("messageType")
                val dataJson = JSONObject(stompMessage.payload).getJSONObject("data").toString()
                Log.d("my log", "data 값 - ${dataJson}")

                val jsonObject = JSONObject(dataJson)

                if (messageType == "messageDetail") { // 메세지 전체 출력일 때
                    val chatMessageArray = jsonObject.getJSONArray("chatMessageList")

                    val chatMessageList = mutableListOf<RoomMessageElementResponseDto>()

                    for (i in 0 until chatMessageArray.length()) {
                        val chatMessageObject = chatMessageArray.getJSONObject(i)
                        val userObject = chatMessageObject.getJSONObject("user")
                        val user = ChatUserResponseDto(
                            sessionId = userObject.getString("sessionId"),
                            name = userObject.getString("name"),
                            type = userObject.getString("type"),
                            profile = userObject.getString("profile")
                        )
                        val content = chatMessageObject.getString("content")
                        val time = chatMessageObject.getString("time")

                        val chatMessage = RoomMessageElementResponseDto(user, content, time)
                        chatMessageList.add(chatMessage)
                    }

                    adapter.setData(chatMessageList)

                } else { // 하나씩 들어올 때
                    val user = ChatUserResponseDto( // 수정 필요
                        sessionId = "",
                        name = jsonObject.getString("userName"),
                        type = "null",
                        profile = ""
                    )
                    val content = jsonObject.getString("content")
                    val time = jsonObject.getString("time")

                    val newChat = RoomMessageElementResponseDto(user, content, time)

                    adapter.updateData(newChat)
                }

                // RecyclerView의 스크롤을 맨 아래로 이동
                binding.chattingRecyclerview.scrollToPosition(adapter.itemCount - 1)

            }, { throwable ->
                Log.e("Stomp subscribe error", "Error on subscribe: ${throwable.localizedMessage}", throwable)
            })
        )

        compositeDisposable.add(stompClient.send("/pub/room/detail/$roomName").subscribe({
            Log.d("Chat", "Chat detail request sent for session $roomName")
        }, { throwable ->
            Log.e("Chat", "Failed to send chat detail request: ${throwable.localizedMessage}", throwable)
        }))


//        stompClient.send("/pub/room/detail/$roomName").subscribe({
//            Log.d("Chat", "Chat detail request sent for session $roomName")
//        }, { throwable ->
//            Log.e("Chat", "Failed to send chat detail request: ${throwable.localizedMessage}", throwable)
//        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stompClient.disconnect()
    }

}