package com.example.sync_front.api_server

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