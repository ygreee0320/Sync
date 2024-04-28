package com.example.sync_front.api_server

import retrofit2.Call
import retrofit2.http.*

interface LoginService {

    // 소셜 로그인
    @POST("/user/signin")
    fun signIn(
        @Header("Authorization") accessToken: String,
        @Body platform: String
    ): Call<LogInResponse>

    // 회원 가입

}