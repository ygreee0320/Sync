package com.example.sync_front.data.model

data class Sync(
    val syncId: Long,
    val syncType: String,
    val type: String,
    val image: String,
    val userCnt: Int,
    val totalCnt: Int,
    val syncName: String,
    val location: String,
    val date: String,
    val associate: String?
)

data class SyncDetail(
    val syncName: String,
    val syncImage: String,
    val syncType: String,
    val type: String,
    val syncIntro: String,
    val date: String,
    val regularDate: String?,
    val location: String,
    val userCnt: Int,
    val totalCnt: Int,
    val userImage: String,
    val userName: String,
    val university: String,
    val userIntro: String
)

data class GraphData(
    val name: String,
    val percent: Int
)

data class GraphDetails(
    val data: List<GraphData>,
    val status: String
)
data class Review(
    val image: String,
    val name: String,
    val university: String,
    val content: String,
    val date: String
)


