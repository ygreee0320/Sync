package com.example.sync_front.data.service

import com.example.sync_front.data.model.NotificationResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AlarmSaveService {
    @GET("/api/notification")
    fun fetchNotifications(
        @Header("Authorization") authorization: String?,
        @Query("topCategory") topCategory: String
    ): Call<NotificationResponse>
}
