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
