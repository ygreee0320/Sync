package com.example.sync_front.data.service

import com.example.sync_front.data.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface EmailService { // 학교 이메일 인증
    @POST("user/school-emails/verification-requests")
    fun sendEmail(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Body request: EmailRequest
    ): Call<EmailResponse>

    @POST("user/school-emails/verifications")
    fun sendCode(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Body request: CodeRequest
    ): Call<CodeResponse>

    @POST("user/school-emails/reset")
    fun sendReset(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
    ): Call<CodeResetResponse>

    @POST("user/valid-university")
    fun validUniv(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Body request: UnivName
    ): Call<CodeResetResponse>
}