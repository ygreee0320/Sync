package com.example.sync_front.data.service

import com.example.sync_front.data.model.Sync
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface HomeService {
    @POST("sync/friend")
    fun postSyncs(
        @Header("Content-Type") application: String,
        @Body request: SyncRequest
    ): Call<SyncResponse>
}

data class SyncRequest(
    val take: Int?,
    val type: String?
)
data class SyncResponse(
    val status: Int,
    val message: String,
    val data: List<Sync>
)