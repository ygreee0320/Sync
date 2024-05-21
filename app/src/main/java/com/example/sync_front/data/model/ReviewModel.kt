package com.example.sync_front.data.model

data class ReviewModel(
    val syncId: Long,
    val content: String
)

data class ReviewResponseData(
    val syncId: Long,
    val userId: Int,
)

data class ReviewResponse(
    val status: Int,
    val message: String,
    val data: ReviewResponseData
)
