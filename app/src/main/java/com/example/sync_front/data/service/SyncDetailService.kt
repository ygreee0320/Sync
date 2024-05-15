package com.example.sync_front.data.service

import com.example.sync_front.data.model.GraphData
import com.example.sync_front.data.model.GraphDetails
import com.example.sync_front.data.model.Review
import com.example.sync_front.data.model.SyncDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SyncDetailService {
    @GET("sync/detail")
    fun getSyncDetail(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String,
        @Query("syncId") userId: Long
    ): Call<SyncDetailResponse>

    @GET("/api/sync/detail/{graph}")
    fun getGraphData(
        @Path("graph") graphType: String,
        @Query("syncId") syncId: Long,
        @Header("Authorization") authorization: String
    ): Call<GraphResponse>

    @GET("sync/detail/review")
    fun getReviewData(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String,
        @Query("syncId") syncId: Long,
        @Query("take") take: Int
    ): Call<ReviewResponse>

    @GET("sync/detail/recommend")
    fun getSameSyncData(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String,
        @Query("syncId") syncId: Long,
        @Query("take") take: Int
    ): Call<SyncResponse>
}


data class SyncDetailResponse(
    val status: Int,
    val message: String,
    val data: SyncDetail
)

data class GraphResponse(
    val status: Int,
    val message: String,
    val data: GraphDetails
)

data class ReviewResponse(
    val status: Int,
    val message: String,
    val data: List<Review>
)