package com.example.sync_front.data.service

import com.example.sync_front.data.model.MySyncResponse
import com.example.sync_front.data.model.MypageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MypageService {

    @GET("mypage")
    fun mypage(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String
    ): Call<MypageResponse>

    @GET("mypage/mysync")
    fun mySyncList(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String
    ): Call<MySyncResponse>

    @GET("mypage/join")
    fun myJoinList(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String
    ): Call<MySyncResponse>

    @GET("mypage/bookmark")
    fun bookmarkList(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String
    ): Call<MySyncResponse>
}