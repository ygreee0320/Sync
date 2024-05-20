package com.example.sync_front.ui.main.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sync_front.api_server.RetrofitClient
import com.example.sync_front.data.model.Sync
import com.example.sync_front.data.service.AssociateSyncRequest
import com.example.sync_front.data.service.SyncRequest
import com.example.sync_front.data.service.SyncResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Application
import androidx.lifecycle.AndroidViewModel


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val syncs = MutableLiveData<List<Sync>>()
    val associateSyncs = MutableLiveData<List<Sync>>()
    val recommendSyncs = MutableLiveData<List<Sync>>()
    val authToken: String?

    init {
        val sharedPreferences = application.getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)
    }

    val errorMessage = MutableLiveData<String>()
    private val _text = MutableLiveData<String>().apply {
        value = "박시윤"

    }
    val text: LiveData<String> = _text
    fun fetchSyncs(take: Int?, type: String? = null) {
        val request = SyncRequest(take, type)
        RetrofitClient.instance.homeService.postFriendSyncs("application/json", authToken, request)
            .enqueue(object : Callback<SyncResponse> {
                override fun onResponse(
                    call: Call<SyncResponse>,
                    response: Response<SyncResponse>
                ) {
                    if (response.isSuccessful) {
                        syncs.postValue(response.body()?.data ?: listOf())
                    } else {
                        if (response.code() == 401) { // 토큰 만료 체크
                            errorMessage.postValue("Token expired. Please log in again.")
                        } else {
                            errorMessage.postValue("Error: ${response.errorBody()?.string()}")
                        }
                    }
                }

                override fun onFailure(call: Call<SyncResponse>, t: Throwable) {
                    errorMessage.postValue(t.message ?: "Unknown error")
                }
            })
    }

    fun fetchAssociateSyncs(take: Int?, syncType: String? = null, type: String? = null) {
        val request = AssociateSyncRequest(take, syncType, type)
        RetrofitClient.instance.homeService.postAssociateSyncs(
            "application/json",
            authToken,
            request
        )
            .enqueue(object : Callback<SyncResponse> {
                override fun onResponse(
                    call: Call<SyncResponse>,
                    response: Response<SyncResponse>
                ) {
                    if (response.isSuccessful) {
                        associateSyncs.postValue(response.body()?.data ?: listOf())
                    } else {
                        if (response.code() == 401) { // 토큰 만료 체크
                            errorMessage.postValue("Token expired. Please log in again.")
                        } else {
                            errorMessage.postValue("Error: ${response.errorBody()?.string()}")
                        }
                    }
                }

                override fun onFailure(call: Call<SyncResponse>, t: Throwable) {
                    errorMessage.postValue(t.message ?: "Unknown error")
                }
            })
    }

    fun fetchRecommendSyncs(userId: Long) {
        RetrofitClient.instance.homeService.getRecommendSyncs("application/json", authToken, userId)
            .enqueue(object : Callback<SyncResponse> {
                override fun onResponse(
                    call: Call<SyncResponse>,
                    response: Response<SyncResponse>
                ) {
                    if (response.isSuccessful) {
                        recommendSyncs.postValue(response.body()?.data ?: listOf())
                    } else {
                        if (response.code() == 401) { // 토큰 만료 체크
                            errorMessage.postValue("Token expired. Please log in again.")
                        } else {
                            errorMessage.postValue("Error: ${response.errorBody()?.string()}")
                        }
                    }
                }

                override fun onFailure(call: Call<SyncResponse>, t: Throwable) {
                    errorMessage.postValue(t.message ?: "Unknown error")
                }
            })
    }

}
