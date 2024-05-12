package com.example.sync_front.ui.onboarding

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.api_server.Onboarding
import com.example.sync_front.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    private lateinit var language: String
    private var profile: String?= ""  // 프로필
    private var profileUri: Uri?= null  // 프로필 uri
    private lateinit var name: String

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달된 데이터 읽기
        language = arguments?.getString("language")!!
        Log.d("my log 받은값 - ", "$language")

        setupUser()
        setupClickListeners()
        setUpChangedListener()
    }

    private fun setupUser() {
        // 소셜 로그인으로 얻은 유저이름, 프로필 꺼내기
        val sharedPreferences = requireActivity().getSharedPreferences("my_token", Context.MODE_PRIVATE)
        name = sharedPreferences.getString("name", null)!!

        binding.explainName.setText(name)
        binding.username.setText(name)
    }

    private fun setupClickListeners() {
        binding.profileImg.setOnClickListener {
            singleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                name = binding.username.text.toString()
                profile = profileUri?.toString()

                val action = ProfileFragmentDirections.actionProfileFragmentToNationalFragment(
                    Onboarding(language, profile, name, null, null, null, null, null)
                )
                findNavController().navigate(action)
            }
        }

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }

        binding.textCancel.setOnClickListener {
            binding.username.setText("") // 텍스트 초기화
        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun setUpChangedListener() { // 이름에 값이 들어갈 때 다음 버튼 활성화
        binding.username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.doneBtn.isEnabled = s?.isNotBlank() ?: false
                updateDoneButtonBackground()
            }
        })
    }

    private fun updateDoneButtonBackground() {
        if (binding.doneBtn.isEnabled) { // 다음 버튼 스타일 변경
            binding.doneBtn.setTextColor(context!!.resources.getColor(R.color.white))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_default)
        } else {
            binding.doneBtn.setTextColor(context!!.resources.getColor(R.color.gray_70))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_gray_10)
        }
    }

    private fun hideKeyboard() {
        binding.username.clearFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}