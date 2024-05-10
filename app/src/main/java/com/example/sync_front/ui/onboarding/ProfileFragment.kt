package com.example.sync_front.ui.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.sync_front.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var language: String
    private var profile: Uri?= null  // 이미지 추가

    private val singleImagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                profile = uri

                binding.profileImg.setImageURI(uri)

                // URI에 대한 지속적인 권한을 부여
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                requireContext().contentResolver.takePersistableUriPermission(uri, flag)
            } else {
                Toast.makeText(requireContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.doneBtn.isEnabled = false

        // 전달된 데이터 읽기
        language = arguments?.getString("language")!!
        Log.d("my log 받은값 - ", "$language")

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.profileImg.setOnClickListener {
            singleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToNationalFragment(language, "이름")
                findNavController().navigate(action)
            }
        }

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }

        binding.closeBtn.setOnClickListener {
            requireActivity().finish() // OnboardingActivity 종료
        }
    }
}