package com.example.sync_front.data.service

import com.example.sync_front.data.model.ReviewModel
import com.example.sync_front.data.model.ReviewResponse
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
}