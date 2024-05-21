package com.example.sync_front.ui.open

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import retrofit2.Call
import retrofit2.Response
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.sync_front.api_server.RetrofitClient
import com.example.sync_front.databinding.FragmentOpenReaderBinding
import com.example.sync_front.ui.main.MainActivity
import com.example.sync_front.data.model.OpenSync
import com.example.sync_front.data.model.PostOpenSync
import com.example.sync_front.data.model.SharedOpenSyncData
import com.example.sync_front.data.service.OpenResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Callback
import java.io.File
import java.io.FileOutputStream
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch

class OpenReaderFragment : Fragment() {
    private var _binding: FragmentOpenReaderBinding? = null
    private val binding get() = _binding!!
    private var authToken: String? = null // 로그인 토큰


    private var profileUri: Uri? = null  // 프로필 uri
    private val singleImagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                profileUri = uri

                binding.profileImg.setImageURI(uri)

                // URI에 대한 지속적인 권한을 부여
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                requireContext().contentResolver.takePersistableUriPermission(uri, flag)
            } else {
                Toast.makeText(requireContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenReaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.doneBtn.setOnClickListener {
            uploadData()
        }

        binding.profileImg.setOnClickListener {
            singleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
    }


    private fun uploadData() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        // 이미지 파일 처리
        val uri = profileUri
        val imageFile = if (uri != null) {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().cacheDir, "upload_image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file
        } else {
            null
        }

        if (imageFile != null) {
            lifecycleScope.launch {
                try {
                    val compressedImage = Compressor.compress(requireContext(), imageFile) {
                        resolution(800, 600)  // 해상도를 800 x 600으로 설정
                        quality(50)  // 압축 품질을 50%로 설정
                    }

                    // 압축된 이미지에 대한 요청 바디 생성
                    val requestFile =
                        compressedImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData(
                        "image",
                        compressedImage.name,
                        requestFile
                    )

                    // Prepare the OpenSync object as before
                    val sync = SharedOpenSyncData(
                        userIntro = "사용자 소개",
                        syncIntro = "모임 소개",
                        syncType = "내친소",
                        syncName = "모임 이름",
                        image = compressedImage.absolutePath,
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

                    val gson = Gson()
                    val syncJson = gson.toJson(sync)
                    val requestBody =
                        syncJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                    // Perform the network request as before
                    RetrofitClient.instance.openSyncService.uploadSyncData(
                        authToken,
                        imagePart,
                        requestBody
                    ).enqueue(object : Callback<OpenResponse> {
                        override fun onResponse(
                            call: Call<OpenResponse>,
                            response: Response<OpenResponse>
                        ) {
                            if (response.isSuccessful) {
                                startActivity(Intent(activity, MainActivity::class.java))
                                activity?.finish()
                            } else {
                                Log.e("Upload", "Failed: ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(call: Call<OpenResponse>, t: Throwable) {
                            Log.e("Upload", "Error: ${t.message}")
                        }
                    })
                } catch (e: Exception) {
                    Toast.makeText(context, "압축 오류: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "Image file is not available.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
