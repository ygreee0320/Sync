package com.example.sync_front.data.service

import com.example.sync_front.data.model.BookmarkRequest
import com.example.sync_front.data.model.BookmarkResponse
import com.example.sync_front.data.model.ReviewModel
import com.example.sync_front.data.model.ReviewResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ReviewService {
    @POST("mypage/review")
    fun sendReview(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Body request: ReviewModel
    ): Call<ReviewResponse>

    @POST("sync/detail/bookmark")
    fun sendBookmark(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Body request: BookmarkRequest
    ): Call<BookmarkResponse>
}