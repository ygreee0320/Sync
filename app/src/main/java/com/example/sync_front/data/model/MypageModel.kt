package com.example.sync_front.data.model

data class MypageModel(
    val userId: Int,
    val image: String,
    val name: String,
    val university: String,
    val syncType: String,
    val detailTypes: List<String>,
    val gender: String
)

data class MypageResponse(
    val status: Int,
    val message: String,
    val data: MypageModel
)

data class MySyncResponse(
    val status: Int,
    val message: String,
    val data: List<Sync>
)