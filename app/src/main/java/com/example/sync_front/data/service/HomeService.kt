package com.example.sync_front.data.service

import com.example.sync_front.data.model.Sync
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeService {
    @POST("sync/friend")
    fun postFriendSyncs(
        @Header("Content-Type") application: String,
        @Body request: SyncRequest
    ): Call<SyncResponse>

    @POST("sync/associate")
    fun postAssociateSyncs(
        @Header("Content-Type") application: String,
        @Body request: AssociateSyncRequest
    ): Call<SyncResponse>


    @GET("sync/recommend")
    fun getRecommendSyncs(
        @Header("Content-Type") contentType: String = "application/json",
        //@Header("Authorization") authorization: String,
        @Query("userId") userId: Long
    ): Call<SyncResponse>

}

data class SyncRequest(
    val take: Int?,
    val type: String?
)

data class AssociateSyncRequest(
    val take: Int?,
    val syncType: String?,
    val type: String?
)

data class SyncResponse(
    val status: Int,
    val message: String,
    val data: List<Sync>
)