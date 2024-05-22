package com.example.sync_front.ui.open

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.sync_front.api_server.RetrofitClient
import com.example.sync_front.data.model.SharedOpenSyncData
import com.example.sync_front.data.service.OpenResponse
import com.example.sync_front.databinding.FragmentOpenPreviewBinding
import com.example.sync_front.ui.main.MainActivity
import com.google.gson.Gson
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import io.reactivex.Observer
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


class OpenPreviewFragment : Fragment() {
    private var _binding: FragmentOpenPreviewBinding? = null
    private val binding get() = _binding!!
    private var authToken: String? = null // 로그인 토큰
    private val openViewModel: OpenViewModel by activityViewModels()
    private var profileUri: Uri? = null  // 프로필 uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnOpen.setOnClickListener {
            uploadData()
        }
    }


    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d("OpenPreviewFragment", "Received sync type: ${data.syncType}")
            Log.d("OpenPreviewFragment", "Received syncName: ${data.syncName}")
            Log.d("OpenPreviewFragment", "Received image: ${data.image}")
            Log.d("OpenPreviewFragment", "Received syncIntro: ${data.syncIntro}")
            Log.d("OpenPreviewFragment", "Received date: ${data.date}")
            Log.d("OpenPreviewFragment", "Received regularDay: ${data.regularDay}")
            Log.d("OpenPreviewFragment", "Received regularTime: ${data.regularTime}")
            Log.d("OpenPreviewFragment", "Received location: ${data.location}")
            Log.d("OpenPreviewFragment", "Received member_min: ${data.member_min}")
            Log.d("OpenPreviewFragment", "Received member_max: ${data.member_max}")
            Log.d("OpenPreviewFragment", "Received userIntro: ${data.userIntro}")
        }

        profileUri = openViewModel.sharedData.value?.image.toString().toUri()

        //텍스트 뷰에 데이터 넣기//
        val currentData = openViewModel.sharedData.value ?: SharedOpenSyncData()
        (binding).apply {
            Glide.with(ivSyncImg.context)
                .load(currentData.image)
                .into(ivSyncImg)
            tvSyncName.text = currentData.syncName
            syncLabel1.text = currentData.syncType
            syncLabel2.text = currentData.type
            tvSyncIntro.text = currentData.syncIntro
            if (currentData.regularDay == null) {
                tvDateTitle.text = "일시"
                tvDate.text = currentData.date
            } else {
                // regularDate가 null이 아니면, regularDate 값을 텍스트로 설정
                tvDate.text = "${currentData.date}"
            }
            tvLocation.text = currentData.location
            tvCnt.text = "최소 ${currentData.member_min}명 최대 ${currentData.member_max}명"
            tvLeaderIntro.text = currentData.userIntro
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
                    val currentData = openViewModel.sharedData.value ?: SharedOpenSyncData()

                    // Prepare the OpenSync object as before
                    val sync = SharedOpenSyncData(
                        userIntro = currentData.userIntro,
                        syncIntro = currentData.syncIntro,
                        syncType = currentData.syncType,
                        syncName = currentData.syncName,
                        image = compressedImage.absolutePath,
                        location = currentData.location,
                        date = currentData.date,
                        regularDay = currentData.regularDay,
                        regularTime = currentData.regularTime,
                        routineDate = currentData.routineDate,
                        member_min = currentData.member_min,
                        member_max = currentData.member_max,
                        type = "액티비티",
                        detailType = "tennis"
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
        _binding = null // 메모리 누수 방지
    }
}