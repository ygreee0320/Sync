package com.example.sync_front.data.model

data class SharedOpenSyncData(
    var userIntro: String? = null,
    var syncIntro: String? = null,
    var syncType: String? = null,
    var syncName: String? = null,
    var image: String? = null,
    var location: String? = null,
    var date: String? = null,
    var regularDay: String? = null,
    var regularTime: String? = null,
    var routineDate: String? = null,
    var member_min: Int = 0,
    var member_max: Int = 0,
    var type: String? = null,
    var detailType: String? = null
)

//response
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

data class PostOpenSync(
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

