package com.example.sync_front.data.model
import com.google.gson.annotations.SerializedName

data class AlarmModel(
    val infoId: String,
    val title: String,
    val content: String,
    val detailContent: String,
    val createdDate: String,
)
data class NotificationResponse(
    val status: Int,
    val message: String,
    @SerializedName("data")
    val alarmData: List<AlarmModel>
)