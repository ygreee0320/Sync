package com.example.sync_front.ui.friend

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.sync_front.api_server.RetrofitClient
import com.example.sync_front.data.model.ForeignLanguage
import com.example.sync_front.data.model.Sync
import com.example.sync_front.data.service.SyncRequest
import com.example.sync_front.data.service.SyncResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendViewModel(application: Application) : AndroidViewModel(application) {
    val friendSyncs = MutableLiveData<List<Sync>>()
    val errorMessage = MutableLiveData<String>()
    val authToken: String?

    init {
        val sharedPreferences = application.getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)
    }

    fun fetchSyncs(take: Int?, type: String?, language: String? = null) {
        val request = SyncRequest(take, type, language)
        RetrofitClient.instance.homeService.postFriendSyncs("application/json", authToken, request)
            .enqueue(object : Callback<SyncResponse> {
                override fun onResponse(
                    call: Call<SyncResponse>,
                    response: Response<SyncResponse>
                ) {
                    if (response.isSuccessful) {
                        friendSyncs.postValue(response.body()?.data ?: listOf())
                    } else {
                        errorMessage.postValue("Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<SyncResponse>, t: Throwable) {
                    errorMessage.postValue(t.message ?: "Unknown error")
                }
            })
    }
}
