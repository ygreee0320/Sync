package com.example.sync_front.data.model

data class Comment(
    val commentId: Int,
    val writerImage: String,
    val writerName: String,
    val createdDate: String,
    val content: String,
    val likeCnt: Int,
    val commentedByUser: Boolean
)

data class CommentResponse(
    val status: Int,
    val message: String,
    val data: List<Comment>
)

data class WriteCommentData(
    val commentId: Int,
    val writerImage: String,
    val writerName: String,
    val createdDate: String,
    val content: String,
    val commentedByUser: Boolean
)

data class WriteCommentResponse(
    val status: Int,
    val message: String,
    val data: WriteCommentData
)

data class LikeResponse(
    val status: Int,
    val message: String,
    val data: Boolean
)

data class Content(
    val content: String
)