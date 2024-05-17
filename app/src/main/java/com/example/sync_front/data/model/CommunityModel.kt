package com.example.sync_front.data.model


data class Community(
    val postId: Int,
    val postType: String,
    val writerImage: String,
    val writerName: String,
    val createdDate: String,
    val title: String,
    val content: String,
    val representativeImage: String,
    val likeCnt: Int,
    val commentCnt: Int,
    val postedByUser: Boolean,
    val likedByUser: Boolean
)

data class CommunityResponse(
    val status: Int,
    val message: String,
    val data: List<Community>
)

data class AddCommunity(
    val postType: String,
    val title: String,
    val content: String
)

data class AddCommunityResponseData(
    val postType: String,
    val title: String,
    val content: String,
    val imageUrls: List<String>
)

data class AddCommunityResponse(
    val status: Int,
    val message: String,
    val data: AddCommunityResponseData
)

data class CommunityDetail(
    val postType: String,
    val writerImage: String,
    val writerName: String,
    val createdDate: String,
    val title: String,
    val content: String,
    val likeCnt: Int,
    val commentCnt: Int,
    val imageUrls: List<String>,
    val postedByUser: Boolean,
    val likedByUser: Boolean
)

data class CommunityDetailResponse(
    val status: Int,
    val message: String,
    val data: CommunityDetail
)

data class CommunitySearchResponse(
    val status: Int,
    val message: String,
    val data: List<CommunitySearchResponseData>
)

data class CommunitySearchResponseData(
    val postId: Int,
    val postType: String,
    val userImage: String,
    val userName: String,
    val title: String,
    val content: String,
    val createdDate: String,
)