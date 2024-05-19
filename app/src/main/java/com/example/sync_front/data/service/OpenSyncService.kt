package com.example.sync_front.data.service

import com.example.sync_front.data.model.OpenSync
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OpenSyncService {
    @Multipart
    @POST("/api/sync")
    fun uploadSyncData(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part,
        @Part("requestDto") requestDto: RequestBody
    ): Call<OpenResponse>
}

data class OpenResponse(
    val status: Int,
    val message: String,
    val data: OpenSync
)