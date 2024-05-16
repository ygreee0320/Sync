package com.example.sync_front.data.service

import com.example.sync_front.data.model.LoginGoogleRequestModel
import com.example.sync_front.data.model.LoginGoogleResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GoogleService {
    // 구글 액세스 토큰 발급
    @POST("oauth2/v4/token")
    fun getAccessToken(
        @Body request: LoginGoogleRequestModel
    ): Call<LoginGoogleResponseModel>
}