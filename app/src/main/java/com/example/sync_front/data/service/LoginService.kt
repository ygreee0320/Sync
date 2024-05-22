package com.example.sync_front.data.service

import com.example.sync_front.data.model.LogInResponse
import com.example.sync_front.data.model.OnboardingRequest
import com.example.sync_front.data.model.OnboardingResponse
import com.example.sync_front.data.model.Platform
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface LoginService {

    // 소셜 로그인
    @POST("auth/signin")
    fun signIn(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Header("fcmToken") fcmToken: String,
        @Body platform: Platform
    ): Call<LogInResponse>

    // 회원 가입
    @Multipart
    @POST("user/onboarding")
    fun onboarding(
        @Header("Authorization") accessToken: String,
        @Part profileImage: MultipartBody.Part?,
        @Part("onBoardingRequest") onBoardingRequest: RequestBody
    ): Call<OnboardingResponse>
}