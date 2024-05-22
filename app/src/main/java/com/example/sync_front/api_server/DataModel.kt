package com.example.sync_front.api_server

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import okhttp3.RequestBody

@Parcelize
data class Onboarding(
    val language: String?,
    val profile: Uri?,
    val userName: String?,
    val countryName: String?,
    val gender: String?,
    val university: String?,
    val syncType: String?,
    val categoryTypes: String?
) : Parcelable

data class Chatting(
    var chattingId: String? = null,
    var user: String? = null,
    var text: String? = null,
    var time: String? = null
)

data class ChattingRoom(
    val roomName: String? = null,
    val syncName: String? = null,
    val total: Int,
    var lastText: String? = null,
    var lastTime: String? = null,
    val image: String
)

data class ChatMessageRequestDto(
    val chatSession: String,
    val fromUserName: String,
    val toRoomName: String,
    val content: String
)

data class image(
    val image: ByteArray
)

data class ChatMessageListRequestDto(
    val chatSession: String,
    val fromUserName: String,
    val toUserName: String,
)

data class RoomMessageListResponseDto(
    val users: List<ChatUserResponseDto>,
    val chatMessageList: List<RoomMessageElementResponseDto> = emptyList()
)

data class ChatUserResponseDto(
    val sessionId: String,
    val name: String,
    val profile: String
)

data class RoomMessageElementResponseDto(
    val user: ChatUserResponseDto,
    val content: String,
    val time: String
)

data class ChattingList(
    val syncName: String,
    val roomName: String,
    val total: Int,
    val content: String,
    val time: String
)

data class ChattingRoomListData(
    val sessionId: String,
    val chatList: List<ChattingList>
)

data class ChattingRoomListResponse(
    val code: Int,
    val messageType: String,
    val data: ChattingRoomListData
)