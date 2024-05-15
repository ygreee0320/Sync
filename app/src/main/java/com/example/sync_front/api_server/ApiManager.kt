package com.example.sync_front.api_server

import android.util.Log
import com.example.sync_front.BuildConfig.GOOGLE_CLIENT_ID
import com.example.sync_front.BuildConfig.GOOGLE_CLIENT_SECRET
import com.google.gson.Gson
import com.kakao.sdk.common.KakaoSdk.type
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginManager {
    fun sendLogin(authToken: String, platform: Platform, callback: (User?) -> Unit) {
        val apiService = RetrofitClient().loginService
        val call = apiService.signIn("application/json", authToken, "1234", platform)

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

object CommunityManager {
    fun getCommunity(authToken: String, type: String, callback: (List<Community>?) -> Unit) {
        val apiService = RetrofitClient().communityService
        val call = apiService.getCommunity("application/json", authToken, type, 0, 10)

        call.enqueue(object : Callback<CommunityResponse> {
            override fun onResponse(call: Call<CommunityResponse>, response: Response<CommunityResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    callback(data!!) // 데이터 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<CommunityResponse>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(null)
            }
        })
    }

    fun getCommunityDetail(authToken: String, postId: Int, callback: (CommunityDetail?) -> Unit) {
        val apiService = RetrofitClient().communityService
        val call = apiService.getCommunityDetail("application/json", authToken, postId)

        call.enqueue(object : Callback<CommunityDetailResponse> {
            override fun onResponse(call: Call<CommunityDetailResponse>, response: Response<CommunityDetailResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    callback(data!!) // 데이터 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<CommunityDetailResponse>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(null)
            }
        })
    }

    fun postCommunity(authToken: String, images: List<MultipartBody.Part>?, community: AddCommunity, callback: (Int?) -> Unit) {
        val apiService = RetrofitClient().communityService
        // AddCommunity 객체를 JSON으로 변환
        val gson = Gson()
        val communityJson = gson.toJson(community)
        val communityRequestBody = communityJson.toRequestBody("application/json".toMediaTypeOrNull())

        val call = apiService.postCommunity("multipart/form-data", authToken, images, communityRequestBody)

        //val call = apiService.postCommunity("multipart/form-data", authToken, images, community)

        call.enqueue(object : Callback<AddCommunityResponse> {
            override fun onResponse(call: Call<AddCommunityResponse>, response: Response<AddCommunityResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.status
                    callback(data!!) // 데이터 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<AddCommunityResponse>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(null)
            }
        })
    }

    fun getCommentList(authToken: String, postId: Int, callback: (List<Comment>?) -> Unit) {
        val apiService = RetrofitClient().communityService
        val call = apiService.getComment("application/json", authToken, postId)

        call.enqueue(object : Callback<CommentResponse> {
            override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    callback(data!!) // 데이터 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(null)
            }
        })
    }
}

object CountriesManager {
    fun getCountries(requestModel: CountriesRequestModel, callback: (List<String>?) -> Unit) {
        val apiService = RetrofitClient().countriesService
        val call = apiService.getCountries("application/json", requestModel)

        call.enqueue(object : Callback<CountriesResponse> {
            override fun onResponse(call: Call<CountriesResponse>, response: Response<CountriesResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    callback(data!!) // 데이터 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<CountriesResponse>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(null)
            }
        })
    }
}

object EmailManager {
    fun sendEmail(request: EmailRequest, callback: (Int?) -> Unit) {
        val apiService = RetrofitClient().emailService
        val call = apiService.sendEmail("application/json", request)

        call.enqueue(object : Callback<EmailResponse> {
            override fun onResponse(call: Call<EmailResponse>, response: Response<EmailResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.status
                    callback(data!!) // 데이터 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(null)
            }
        })
    }

    fun sendCode(request: CodeRequest, callback: (Int?) -> Unit) {
        val apiService = RetrofitClient().emailService
        val call = apiService.sendCode("application/json", request)

        call.enqueue(object : Callback<CodeResponse> {
            override fun onResponse(call: Call<CodeResponse>, response: Response<CodeResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.status
                    callback(data!!) // 데이터 전달
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<CodeResponse>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
                callback(null)
            }
        })
    }

    fun resetCode() {
        val apiService = RetrofitClient().emailService
        val call = apiService.sendReset("application/json")

        call.enqueue(object : Callback<CodeResetResponse> {
            override fun onResponse(call: Call<CodeResetResponse>, response: Response<CodeResetResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.message
                    Log.d("my log", "$data")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("서버 테스트", "오류1: $errorBody")
                }
            }

            override fun onFailure(call: Call<CodeResetResponse>, t: Throwable) {
                Log.e("서버 테스트", "오류2: ${t.message}")
            }
        })
    }
}