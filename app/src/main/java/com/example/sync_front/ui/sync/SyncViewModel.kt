package com.example.sync_front.ui.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sync_front.api_server.RetrofitClient
import com.example.sync_front.data.model.SyncDetail
import com.example.sync_front.data.service.SyncDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SyncViewModel : ViewModel() {
    private val _syncDetail = MutableLiveData<SyncDetail>()
    val syncDetail: LiveData<SyncDetail> = _syncDetail

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchSyncDetail(syncId: Long, authToken: String) {
        RetrofitClient.instance.syncDetailService.getSyncDetail("application/json", authToken, syncId)
            .enqueue(object : Callback<SyncDetailResponse> {
                override fun onResponse(call: Call<SyncDetailResponse>, response: Response<SyncDetailResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let {
                            _syncDetail.postValue(it)
                        } ?: run {
                            _errorMessage.postValue("No sync detail found")
                        }
                    } else {
                        _errorMessage.postValue("Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<SyncDetailResponse>, t: Throwable) {
                    _errorMessage.postValue(t.message ?: "Unknown error")
                }
            })
    }
}