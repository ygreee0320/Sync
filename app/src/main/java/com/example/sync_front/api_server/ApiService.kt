package com.example.sync_front.api_server

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface LoginService {

    // 소셜 로그인
    @POST("user/signin")
    fun signIn(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Body platform: Platform
    ): Call<LogInResponse>

    // 회원 가입

}

interface GoogleService {
    // 구글 액세스 토큰 발급
    @POST("oauth2/v4/token")
    fun getAccessToken(
        @Body request: LoginGoogleRequestModel
    ): Call<LoginGoogleResponseModel>
}