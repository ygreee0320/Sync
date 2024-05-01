package com.example.sync_front.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("updateToken")
    fun updateToken(@Body tokenUpdateRequest: TokenUpdateRequest): Call<Void>
}

data class TokenUpdateRequest(val token: String)