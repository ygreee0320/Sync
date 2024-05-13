package com.example.sync_front.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sync_front.api_server.RetrofitClient
import com.example.sync_front.data.model.Sync
import com.example.sync_front.data.service.AssociateSyncRequest
import com.example.sync_front.data.service.SyncRequest
import com.example.sync_front.data.service.SyncResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel : ViewModel() {
    val syncs = MutableLiveData<List<Sync>>()
    val associateSyncs = MutableLiveData<List<Sync>>()
    val errorMessage = MutableLiveData<String>()
    private val _text = MutableLiveData<String>().apply {
        value = "박시윤"

    }
    val text: LiveData<String> = _text

    fun fetchSyncs(take: Int?, type: String? = null) {
        val request = SyncRequest(take, type)
        RetrofitClient.instance.homeService.postFriendSyncs("application/json", request)
            .enqueue(object : Callback<SyncResponse> {
                override fun onResponse(
                    call: Call<SyncResponse>,
                    response: Response<SyncResponse>
                ) {
                    if (response.isSuccessful) {
                        syncs.postValue(response.body()?.data ?: listOf())
                    } else {
                        errorMessage.postValue("Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<SyncResponse>, t: Throwable) {
                    errorMessage.postValue(t.message ?: "Unknown error")
                }
            })
    }

    fun fetchAssociateSyncs(take: Int?, syncType: String? = null, type: String? = null) {
        val request = AssociateSyncRequest(take, syncType, type)
        RetrofitClient.instance.homeService.postAssociateSyncs("application/json", request)
            .enqueue(object : Callback<SyncResponse> {
                override fun onResponse(
                    call: Call<SyncResponse>,
                    response: Response<SyncResponse>
                ) {
                    if (response.isSuccessful) {
                        associateSyncs.postValue(response.body()?.data ?: listOf())
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
