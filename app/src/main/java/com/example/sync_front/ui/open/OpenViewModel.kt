package com.example.sync_front.ui.open

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sync_front.api_server.RetrofitClient
import com.example.sync_front.data.model.PostOpenSync
import com.example.sync_front.data.model.SharedOpenSyncData
import com.example.sync_front.data.service.OpenResponse
import com.google.gson.Gson
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class OpenViewModel : ViewModel() {
    val sharedData = MutableLiveData<SharedOpenSyncData>()

    fun updateData(data: SharedOpenSyncData) {
        sharedData.postValue(data)
    }
}
