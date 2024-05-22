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
/*
    //여기부터는 아직 안 함
    var authToken: String? = null
    var profileUri: Uri? = null

    fun uploadData(context: Context) {
        val imageFile = prepareImageFile(context, profileUri)

        imageFile?.let {
            compressAndUpload(context, it)
        } ?: run {
            // Handle error, e.g., show a toast message
        }
    }

    private fun prepareImageFile(context: Context, uri: Uri?): File? {
        return uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val file = File(context.cacheDir, "upload_image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file
        }
    }

    private fun compressAndUpload(context: Context, imageFile: File) {
        viewModelScope.launch {
            try {
                val compressedImage = Compressor.compress(context, imageFile) {
                    resolution(800, 600)
                    quality(50)
                }

                createRequestBodyAndUpload(compressedImage)
            } catch (e: Exception) {
                // Handle compression error
            }
        }
    }

    private fun createRequestBodyAndUpload(compressedImage: File) {
        val requestFile = compressedImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", compressedImage.name, requestFile)
        val sync = createPostOpenSyncObject(compressedImage)

        val gson = Gson()
        val syncJson = gson.toJson(sync)
        val requestBody = syncJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        RetrofitClient.instance.openSyncService.uploadSyncData(
            authToken,
            imagePart,
            requestBody
        ).enqueue(object : Callback<OpenResponse> {
            override fun onResponse(call: Call<OpenResponse>, response: Response<OpenResponse>) {
                // Handle successful upload
            }

            override fun onFailure(call: Call<OpenResponse>, t: Throwable) {
                // Handle upload failure
            }
        })
    }

    private fun createPostOpenSyncObject(image: File): PostOpenSync {
        return PostOpenSync(
            userIntro = "사용자 소개",
            syncIntro = "모임 소개",
            syncType = "내친소",
            syncName = "모임 이름",
            image = image.absolutePath,
            location = "서울시 성북구",
            date = "2023-04-13 15:30",
            regularDay = "수",
            regularTime = "15:30",
            routineDate = "2023-04-13 15:30",
            member_min = 5,
            member_max = 10,
            type = "외국어",
            detailType = "languageExchange"
        )
    }

    */

}
