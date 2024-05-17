package com.example.sync_front.ui.sync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sync_front.api_server.RetrofitClient
import com.example.sync_front.data.model.GraphData
import com.example.sync_front.data.model.SyncDetail
import com.example.sync_front.data.service.SyncDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.sync_front.data.model.GraphDetails
import com.example.sync_front.data.model.Review
import com.example.sync_front.data.model.Sync
import com.example.sync_front.data.service.GraphResponse
import com.example.sync_front.data.service.ReviewResponse
import com.example.sync_front.data.service.SyncResponse


class SyncViewModel : ViewModel() {
    private val _syncDetail = MutableLiveData<SyncDetail>()
    val syncDetail: LiveData<SyncDetail> = _syncDetail

    // ViewModel 내에 LiveData 정의 변경
    private val _graphDetails = MutableLiveData<GraphDetails>()
    val graphDetails: LiveData<GraphDetails> = _graphDetails

    private val reviewLiveData = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = reviewLiveData

    private val sameSyncLiveData = MutableLiveData<List<Sync>>()
    val sames: LiveData<List<Sync>> = sameSyncLiveData


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchSyncDetail(syncId: Long, authToken: String) {
        RetrofitClient.instance.syncDetailService.getSyncDetail(
            "application/json",
            authToken,
            syncId
        )
            .enqueue(object : Callback<SyncDetailResponse> {
                override fun onResponse(
                    call: Call<SyncDetailResponse>,
                    response: Response<SyncDetailResponse>
                ) {
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

    fun fetchGraphData(graphType: String, syncId: Long, token: String) {
        RetrofitClient.instance.syncDetailService.getGraphData(graphType, syncId, token)
            .enqueue(object : Callback<GraphResponse> {
                override fun onResponse(
                    call: Call<GraphResponse>,
                    response: Response<GraphResponse>
                ) {
                    if (response.isSuccessful) {
                        _graphDetails.postValue(response.body()?.data)
                    } else {
                        _errorMessage.postValue(
                            "Error fetching graph data: ${
                                response.errorBody()?.string()
                            }"
                        )
                    }
                }

                override fun onFailure(call: Call<GraphResponse>, t: Throwable) {
                    _errorMessage.postValue(t.message ?: "Network error occurred")
                }
            })
    }

    fun fetchReviews(token: String, syncId: Long, take: Int) {
        RetrofitClient.instance.syncDetailService.getReviewData(
            "application/json",
            token,
            syncId,
            take
        )
            .enqueue(object : Callback<ReviewResponse> {
                override fun onResponse(
                    call: Call<ReviewResponse>,
                    response: Response<ReviewResponse>
                ) {
                    if (response.isSuccessful) {
                        reviewLiveData.postValue(response.body()?.data)
                    } else {
                        // Handle errors
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    // Handle failure
                }
            })
    }

    fun fetchSameSyncData(authToken: String, syncId: Long, take: Int) {
        RetrofitClient.instance.syncDetailService.getSameSyncData(
            contentType = "application/json",
            authorization = authToken,
            syncId = syncId,
            take = take
        ).enqueue(object : Callback<SyncResponse> {
            override fun onResponse(call: Call<SyncResponse>, response: Response<SyncResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { syncData ->
                        sameSyncLiveData.postValue(syncData)
                    } ?: run {
                        _errorMessage.postValue("No sync data found")
                    }
                } else {
                    _errorMessage.postValue(
                        "Error fetching sync data: ${
                            response.errorBody()?.string()
                        }"
                    )
                }
            }

            override fun onFailure(call: Call<SyncResponse>, t: Throwable) {
                _errorMessage.postValue(t.message ?: "Network error occurred")
            }
        })
    }

}