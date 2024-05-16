package com.example.sync_front.data.service

import com.example.sync_front.data.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface EmailService { // 학교 이메일 인증
    @POST("auth/school-emails/verification-requests")
    fun sendEmail(
        @Header("Content-Type") application: String,
        @Body request: EmailRequest
    ): Call<EmailResponse>

    @POST("auth/school-emails/verifications")
    fun sendCode(
        @Header("Content-Type") application: String,
        @Body request: CodeRequest
    ): Call<CodeResponse>

    @POST("auth/school-emails/reset")
    fun sendReset(
        @Header("Content-Type") application: String
    ): Call<CodeResetResponse>
}