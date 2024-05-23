package com.example.sync_front.ui.open

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.sync_front.R
import com.example.sync_front.api_server.MypageManager
import com.example.sync_front.api_server.RetrofitClient
import com.example.sync_front.data.model.SharedOpenSyncData
import com.example.sync_front.data.service.OpenResponse
import com.example.sync_front.databinding.FragmentOpenPreviewBinding
import com.example.sync_front.databinding.PopupOpenSyncBinding
import com.example.sync_front.databinding.PopupOpenSyncNotifyBinding
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
        initialSetting()

    }

    private fun setupClickListeners() {
        binding.btnOpen.setOnClickListener {
            showPopup1()
        }
    }

    private fun initialSetting() {
        // 저장된 토큰 읽어오기
        val sharedPreferences =
            requireActivity().getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)
        loadMe()
    }

    private fun showPopup1() {
        val popupLayoutBinding = PopupOpenSyncNotifyBinding.inflate(layoutInflater)
        val popupView = popupLayoutBinding.root

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(popupView)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        popupLayoutBinding.cancelBtn.setOnClickListener {
            alertDialog.dismiss() // 팝업 닫기
        }
        popupLayoutBinding.openBtn.setOnClickListener {
            alertDialog.dismiss()
            uploadData()

        }

    }

    private fun showPopup2() {
        val popupLayoutBinding = PopupOpenSyncBinding.inflate(layoutInflater)
        val popupView = popupLayoutBinding.root

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(popupView)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        popupLayoutBinding.shareBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        popupLayoutBinding.checkBtn.setOnClickListener {


            alertDialog.dismiss() // 팝업 닫기
            // 현재 액티비티 종료
            requireActivity().finish()

            // MainActivity의 MyFragment로 이동
            val mainActivityIntent = Intent(requireContext(), MainActivity::class.java)
            mainActivityIntent.putExtra(
                "fragment",
                "MyFragment"
            ) // MyFragment로 이동하는 것을 나타내는 플래그나 데이터를 전달할 수 있습니다.
            startActivity(mainActivityIntent)
        }

    }

    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d(javaClass.simpleName, "Received data: $data")
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
            syncLabel2.text = currentData.detailType
            tvSyncIntro.text = currentData.syncIntro
            if (currentData.regularDay == null) {
                tvDateTitle.text = "일시"
                tvDate.text = currentData.date
            } else {
                // regularDate가 null이 아니면, regularDate 값을 텍스트로 설정
                tvDate.text =
                    "매주${currentData.regularDay}  ${currentData.regularTime}\n첫 모임 날짜: ${currentData.routineDate}"
            }
            tvLocation.text = currentData.location
            tvCnt.text = "최소 ${currentData.member_min}명 최대 ${currentData.member_max}명"
            tvLeaderIntro.text = currentData.userIntro
        }
    }

    private fun loadMe() {
        MypageManager.mypage(authToken!!, "한국어") { response ->
            if (response?.status == 200) {
                binding.username.text = response.data.name
                binding.userschool.text = response.data.university

                if (!response.data.image.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(response.data.image)
                        .placeholder(R.drawable.img_profile_default)
                        .error(R.drawable.img_profile_default)
                        .into(binding.profileImg)
                } else {
                    binding.profileImg.setImageResource(R.drawable.img_profile_default)
                }
            }
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
                        detailType = currentData.detailType,
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
                                showPopup2()
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