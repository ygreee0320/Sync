package com.example.sync_front.data.service

import com.example.sync_front.data.model.LogInResponse
import com.example.sync_front.data.model.Platform
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

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

}