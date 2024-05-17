package com.example.sync_front.data.model

data class User(
    val userId: Int,
    val email: String,
    val name: String,
    val picture: String,
    val accessToken: String,
    val refreshToken: String,
    val isFirst: Boolean,
    val sessionId: String
)

data class LogInResponse(
    val status: Int,
    val message: String,
    val data: User
)

data class Platform(
    val platform: String
)