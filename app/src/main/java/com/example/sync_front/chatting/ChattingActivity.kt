package com.example.sync_front.chatting

import ChattingAdapter
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.R
import com.example.sync_front.api_server.*
import com.example.sync_front.databinding.ActivityChattingBinding
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.ByteString
import org.checkerframework.checker.units.qual.s
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompCommand
import ua.naiksoftware.stomp.dto.StompMessage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicBoolean

class ChattingActivity : AppCompatActivity() {
    lateinit var binding: ActivityChattingBinding
    lateinit var myName : String
    lateinit var chattingList: MutableList<RoomMessageElementResponseDto>
    private lateinit var adapter: ChattingAdapter
    private var authToken: String ?= null // 로그인 토큰
    private var plusToggle: Boolean = false // 사진 추가 버튼 활성화
    private var uriList = ArrayList<Uri>()  // 선택한 이미지 uri

    private lateinit var stompClient: StompClient
    private val compositeDisposable = CompositeDisposable()
    val isUnexpectedClosed = AtomicBoolean(false)
    private var roomName: String ?= "eksxhr"
    private var sessionId: String ?= null // 세션id
    private val PICK_IMAGE_REQUEST = 1

    companion object {
        private val maxImage = 5
    }

    private val multipleImagePicker =
        registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(maxImage)) { uris: List<Uri>? ->
            if (uris != null) {
                val totalSelectedImages = uriList.size + uris.size
                if (totalSelectedImages > maxImage) {
                    Toast.makeText(applicationContext, "사진은 ${maxImage}장까지 선택 가능합니다.", Toast.LENGTH_LONG)
                        .show()
                } else {
                    // URI에 대한 지속적인 권한을 부여합니다.
                    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    for (uri in uris) {
                        applicationContext.contentResolver.takePersistableUriPermission(uri, flag)
                    }
                    uriList.addAll(0, uris)
                    Log.d("my log", ""+uriList)

                    val imageParts = uriList.map { uri ->
                        val file = File(getRealPathFromURI(uri))
                        val requestFile = RequestBody.create("images/*".toMediaTypeOrNull(), file)
                        MultipartBody.Part.createFormData("images", file.name, requestFile)
                    }



                    //sendMessage(imageParts, "ㅎㅇ")
                }
            } else {
                Toast.makeText(applicationContext, getString(R.string.didnt_select_img), Toast.LENGTH_LONG).show()
            }
        }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSetting()
        setupClickListeners()

        initStomp()
    }

    private fun initialSetting() {
        // 저장된 토큰 읽어오기
        val sharedPreferences = this.getSharedPreferences("my_token", Context.MODE_PRIVATE)
        //authToken = sharedPreferences.getString("auth_token", null)
        authToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMyIsImlhdCI6MTcxNjEyNDgzOSwiZXhwIjoxNzE2NzI5NjM5fQ.sItyCgXULj4jKP70aKstlh4o1dEVAECcE4Ws-r7mhuE"
        myName = sharedPreferences.getString("name", "익명")!!
        //roomName = intent.getStringExtra("roomName")
        sessionId = sharedPreferences.getString("sessionId", null)

        // 어댑터를 설정하고 리사이클러뷰에 연결
        chattingList = mutableListOf<RoomMessageElementResponseDto>()
        adapter = ChattingAdapter(chattingList, myName)
        binding.chattingRecyclerview.layoutManager = LinearLayoutManager(this@ChattingActivity)
        binding.chattingRecyclerview.adapter = adapter

        val syncName = intent.getStringExtra("syncName")
        binding.chattingTitle.text = syncName

        val total = intent.getIntExtra("total", 0)
        binding.memberCount.text = total.toString()
    }

    private fun setupClickListeners() {
        // 메세지 전송 버튼 클릭 시
        binding.sendBtn.setOnClickListener {
            val message = binding.sendTxt.text.toString().trim()

            if (message.isEmpty()) {
                Toast.makeText(this@ChattingActivity, getString(R.string.input_message), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else { // 메세지가 있다면 전송
                if (stompClient.isConnected()) {
                    Log.d("WebSocket", "연결됨")
                    //sendMessage(message, null) // 전송
                } else {
                    Log.d("WebSocket", "연결되지 않음")
                }
            }
        }

        binding.picBtn.setOnClickListener {
            val remainingImages = maxImage - uriList.size
            if (remainingImages > 0) {
                // 갤러리에서 이미지 선택
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
//                singleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
//                multipleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            } else {
                Toast.makeText(applicationContext, "사진은 ${maxImage}장까지 선택 가능합니다.", Toast.LENGTH_LONG).show()
            }
        }

        // 키보드가 활성화되면 리사이클러뷰의 크기 조정
        binding.chattingRecyclerview.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            binding.chattingRecyclerview.scrollToPosition(adapter.itemCount - 1)
        }

        binding.plusBtn.setOnClickListener {
            if (!plusToggle) {
                plusToggle = !plusToggle
                binding.picBtn.visibility = View.VISIBLE
                //binding.plusBtn.setBackgroundResource(R.drawable.)
            } else {
                plusToggle = !plusToggle
                binding.picBtn.visibility = View.GONE
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("CheckResult")
    private fun initStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://kusitms28.store/ws")

        stompClient.lifecycle().subscribe { lifecycleEvent: LifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d(TAG, "Stomp connection opened")
                    requestAndSubscribeToChatDetails(roomName!!)
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

    private fun sendMessage(message: String, byteString: String) {
        binding.sendTxt.setText("") // 텍스트창 초기화

        val chatMessage = ChatMessageRequestDto(
            "113828093759900814627_ef4a27",
            myName,
            roomName!!,
            message,
            byteString
        ) // 채팅 보내는 사람 정보, 텍스트

        val json = Gson().toJson(chatMessage)

        //val requestBody = imageFile?.readBytes()?.toRequestBody("application/octet-stream".toMediaTypeOrNull())

//        if (imageFile != null) {
//            // 이미지 파일을 바이너리로 읽어와 StompMessage에 추가
//            val requestBody = imageFile.readBytes().toRequestBody("application/octet-stream".toMediaTypeOrNull())
//            val stompMessage = StompMessage(StompCommand.SEND, mapOf("content-type" to "application/octet-stream"), requestBody)
//            stompClient.send(stompMessage).subscribe()
//        } else {
//            // 이미지 파일이 없는 경우 단순 메시지 전송
//            stompClient.send("/pub/room/$roomName", json).subscribe()
//        }


//        val chatMessage2 = image(imageFile)
//        val json2 = Gson().toJson(chatMessage2)
//        stompClient.send("/pub/room/image/$roomName", json2).subscribe()


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
                        profile = jsonObject.getString("profile")
                    )
                    val content = jsonObject.getString("content")
                    val time = jsonObject.getString("time")

                    //val image = jsonObject.getString("images")

                    //Log.d("my log", "받은값- $image")

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
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            it.moveToFirst()
            val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val filePath = it.getString(columnIndex)
            it.close()
            return filePath ?: ""
        }
        return ""
    }

//    private val singleImagePicker =
//        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
//            if (uri != null) {
//                val profileUri = uri
//
//                // URI에 대한 지속적인 권한을 부여
//                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                this.contentResolver.takePersistableUriPermission(uri, flag)
//
//                val byteImage = uriToByteArray(profileUri)
//
//                Log.d("my log", "바이트값- $byteImage")
//
//
//                val hexString = byteImage.joinToString(separator = " ") { byte ->
//                    String.format("%02X", byte)
//                }
//                Log.d("ByteArray", "Content: $hexString")
//                sendMessage("", byteImage)
//
//            } else {
//            }
//        }
//
//    private fun uriToByteArray(uri: Uri): ByteArray {
//        val inputStream: InputStream? = contentResolver.openInputStream(uri)
//        return inputStream?.readBytes() ?: ByteArray(0)
//    }
//
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // 이미지 파일을 비트맵으로 디코딩
            val inputStream = contentResolver.openInputStream(data.data!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // 이미지를 Base64로 인코딩
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes = baos.toByteArray()
            val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)

            //Log.d("Image Bytes", imageBytes.joinToString(" "))
//            Log.d("Image Bytes", imageBytes.size.toString())
            Log.d("Image Bytes", encodedImage)
            Log.d("Image Bytes", encodedImage.length.toString())

            // imageBytes를 100바이트씩 나누어 List<ByteArray>로 변환
            val chunkSize = 100
            val chunks = mutableListOf<String>()
            for (i in imageBytes.indices step chunkSize) {
                val end = (i + chunkSize).coerceAtMost(imageBytes.size)
                val chunk = imageBytes.copyOfRange(i, end)
                val encodedChunk = Base64.encodeToString(chunk, Base64.NO_WRAP)
                chunks.add(encodedChunk)
            }
//
//            // 각 청크를 순차적으로 전송 (예시로 로그에 출력)
//            for ((index, chunk) in chunks.withIndex()) {
//                Log.d("Chunk $index", chunk)
//                //sendMessage("",chunk)
//            }

            // 바이트 데이터를 문자열로 변환하여 로그에 출력
//            val byteString = imageBytes.joinToString(" ") { it.toString() }
//            Log.d("Image Bytes", byteString)

             //바이트 데이터를 문자열로 변환하여 로그에 출력
            val byteString = imageBytes.joinToString(
                prefix = "[", postfix = "]", separator = ", "
            ) { it.toString() }
            Log.d("Image Bytes 문자열", byteString.length.toString())

//            val contentType = "application/json"
//            val requestByte = byteString.toRequestBody(contentType.toMediaTypeOrNull())
            //CommunityManager.getImage(authToken!!, byteString.toRequestBody(contentType.toMediaTypeOrNull()))

            //Log.d("Encoded Image", encodedImage)
            sendMessage("", byteString)

            // 이미지 데이터를 웹소켓을 통해 서버로 전송
            //sendMessage(encodedImage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stompClient.disconnect()
    }

}