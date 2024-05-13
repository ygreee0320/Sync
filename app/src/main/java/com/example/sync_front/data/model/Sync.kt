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
    val location: String,
    val userCnt: Int,
    val totalCnt: Int,
    val userImage: String,
    val userName: String,
    val university: String,
    val userIntro: String
)
