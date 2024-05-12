package com.example.sync_front.api_server

import com.example.sync_front.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class RetrofitClient {

    val interceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    val client = OkHttpClient().newBuilder()
        .addNetworkInterceptor(interceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://www.sync-kusitms29.site/api/")
        .addConverterFactory(GsonConverterFactory.create(getGson()))
        .client(client)
        .build()

    fun getGson(): Gson {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()) // 원하는 시간 형식 지정

        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd") // Date 형식 지정
            .create()
    }

    val loginService = retrofit.create(LoginService::class.java)
    val countriesService = retrofit.create(CountriesService::class.java)
    val emailService = retrofit.create(EmailService::class.java)

}