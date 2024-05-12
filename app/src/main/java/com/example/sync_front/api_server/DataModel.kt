package com.example.sync_front.api_server

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class User(
    val userId: Int,
    val email: String,
    val name: String,
    val picture: String,
    val accessToken: String,
    val refreshToken: String,
    val isFirst: Int,
    val sessionId: String
)

@Parcelize
data class Onboarding(
    val language: String?,
    val profile: String?,
    val userName: String?,
    val countryName: String?,
    val gender: String?,
    val university: String?,
    val syncType: String?,
    val categoryTypes: String?
) : Parcelable

data class Platform(
    val platform: String
)

data class LogInResponse(
    val status: Int,
    val message: String,
    val data: User
)

data class Chatting(
    var chattingId: String? = null,
    var user: String? = null,
    var text: String? = null,
    var time: String? = null
)

data class ChattingRoom(
    var roomId: String? = null,
    var title: String? = null,
    var lastText: String? = null,
    var lastTime: String? = null
)

data class ChatMessageRequestDto(
    val chatSession: String,
    val fromUserName: String,
    val toRoomName: String,
    val content: String
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
    val type: String,
    val profile: String
)

data class RoomMessageElementResponseDto(
    val user: ChatUserResponseDto,
    val content: String,
    val time: String
)

data class CountriesRequestModel(
    val page: String,
    val perPage: String,
    val language: String
)
data class CountriesResponse(
    val status: Int,
    val message: String,
    val data: List<String>
)

data class EmailRequest( // 인증 코드 전송 시 요청 데이터
    val email: String,
    val univName: String
)

data class EmailResponse( // 인증 코드 전송 시 받는 데이터
    val status: Int,
    val message: String
)

data class CodeRequest( // 인증 코드 검증 요청
    val email: String,
    val univName: String,
    val code: String
)

data class CodeResponseData(
    val success: Boolean,
    val univName: String,
    val certified_email: String,
    val certified_date: String
)

data class CodeResponse( // 인증 코드 검증 완료
    val status: Int,
    val message: String,
    val data: CodeResponseData
)

data class CodeResetResponse(
    val status: Int,
    val message: String,
    val data: Boolean
)

data class LoginGoogleRequestModel(
    @SerializedName("grant_type")
    private val grant_type: String,
    @SerializedName("client_id")
    private val client_id: String,
    @SerializedName("client_secret")
    private val client_secret: String,
    @SerializedName("code")
    private val code: String
)

data class LoginGoogleResponseModel(
    @SerializedName("access_token") var access_token: String,
)
