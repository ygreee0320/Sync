package com.example.sync_front.data.model

data class OpenSync(
    val syncId: Int,
    val userIntro: String,
    val syncIntro: String,
    val syncType: String,
    val syncName: String,
    val image: String,
    val location: String,
    val date: String,
    val regularDay: String?,
    val regularTime: String?,
    val routineDate: String?,
    val member_min: Int,
    val member_max: Int,
    val type: String,
    val detailType: String
)
