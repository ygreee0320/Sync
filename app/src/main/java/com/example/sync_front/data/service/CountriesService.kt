package com.example.sync_front.data.service

import com.example.sync_front.data.model.CountriesRequestModel
import com.example.sync_front.data.model.CountriesResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CountriesService { //국가 조회
    @POST("user/countries")
    fun getCountries(
        @Header("Content-Type") application: String,
        @Header("Authorization") accessToken: String,
        @Body request: CountriesRequestModel
    ): Call<CountriesResponse>
}