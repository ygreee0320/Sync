package com.example.sync_front.api_server

import android.util.Log
import com.example.sync_front.BuildConfig.GOOGLE_CLIENT_ID
import com.example.sync_front.BuildConfig.GOOGLE_CLIENT_SECRET
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginManager {
    fun sendLogin(authToken: String, platform: Platform, callback: (User?) -> Unit) {
        val apiService = RetrofitClient().loginService
        val call = apiService.signIn("application/json", authToken, platform)

        call.enqueue(object : Callback<LogInResponse> {
            override fun onResponse(call: Call<LogInResponse>, response: Response<LogInResponse>) {
                if (response.isSuccessful) {
                    val userData = response.body()?.data
                    callback(userData!!) // 사용자 데이터 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<LogInResponse>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(null)
            }
        })
    }

    fun getAccessToken(authCode:String, callback: (String?) -> Unit) { // 구글 액세스 토큰 요청
        val apiService = GoogleClient().loginService

        val call = apiService.getAccessToken(
            request = LoginGoogleRequestModel(
                grant_type = "authorization_code",
                client_id = "${GOOGLE_CLIENT_ID}",
                client_secret = "${GOOGLE_CLIENT_SECRET}",
                code = authCode.orEmpty()
            )
        )
        call.enqueue(object : Callback<LoginGoogleResponseModel> {
            override fun onResponse(call: Call<LoginGoogleResponseModel>, response: Response<LoginGoogleResponseModel>) {
                if(response.isSuccessful) {
                    val accessToken = response.body()?.access_token.orEmpty()
                    Log.d("서버 테스트", "accessToken: $accessToken")
                    callback(accessToken) // 액세스 토큰 전달
                }
            }
            override fun onFailure(call: Call<LoginGoogleResponseModel>, t: Throwable) {
                Log.e("서버 테스트", "getOnFailure: ",t.fillInStackTrace() )
            }
        })
    }
}