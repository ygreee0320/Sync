package com.example.sync_front.data.service

import com.example.sync_front.data.model.CheckLanguageRequest
import com.example.sync_front.data.model.CheckLanguageResponse
import com.example.sync_front.data.model.TranslateRequest
import com.example.sync_front.data.model.TranslateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TranslationService {
    // 현재 언어 감지
    @POST("community/check-language")
    fun checkLanguage(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Body query: CheckLanguageRequest
    ): Call<CheckLanguageResponse>

    // 번역
    @POST("community/translate")
    fun translate(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Body request: TranslateRequest
    ): Call<TranslateResponse>
}