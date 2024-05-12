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
        @Header("fcmToken") fcmToken: String,
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

interface CountriesService { //국가 조회
    @POST("user/countries")
    fun getCountries(
        @Header("Content-Type") application: String,
        @Body request: CountriesRequestModel
    ): Call<CountriesResponse>
}

interface EmailService { // 학교 이메일 인증
    @POST("user/school-emails/verification-requests")
    fun sendEmail(
        @Header("Content-Type") application: String,
        @Body request: EmailRequest
    ): Call<EmailResponse>

    @POST("user/school-emails/verifications")
    fun sendCode(
        @Header("Content-Type") application: String,
        @Body request: CodeRequest
    ): Call<CodeResponse>

    @POST("user/school-emails/reset")
    fun sendReset(
        @Header("Content-Type") application: String
    ): Call<CodeResetResponse>
}