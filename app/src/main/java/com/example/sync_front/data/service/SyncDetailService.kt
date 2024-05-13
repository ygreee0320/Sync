package com.example.sync_front.data.service

import com.example.sync_front.data.model.SyncDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SyncDetailService {
    @GET("sync/detail")
    fun getSyncDetail(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String,
        @Query("syncId") userId: Long
    ): Call<SyncDetailResponse>
}

data class SyncDetailResponse(
    val status: Int,
    val message: String,
    val data: SyncDetail
)