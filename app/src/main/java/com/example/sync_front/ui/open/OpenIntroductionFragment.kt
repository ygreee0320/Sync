package com.example.sync_front.ui.open

import android.app.DatePickerDialog
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
import androidx.fragment.app.activityViewModels
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenIntroductionBinding
import androidx.navigation.fragment.findNavController
import com.example.sync_front.data.model.SharedOpenSyncData
import com.example.sync_front.databinding.PopupCancleSyncBinding
import androidx.appcompat.app.AlertDialog

class OpenIntroductionFragment : Fragment() {
    private var _binding: FragmentOpenIntroductionBinding? = null
    private val binding get() = _binding!!
    private var profile: String? = ""  // 프로필
    private var profileUri: Uri? = null  // 프로필 uri
    private lateinit var intro: String
    private val openViewModel: OpenViewModel by activityViewModels()

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
        _binding = FragmentOpenIntroductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.doneBtn.isEnabled = false
        setupClickListeners()
        setUpChangedListener()
        observeViewModel()
    }

    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d(javaClass.simpleName, "Received data: $data")
        }
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            showPopup()
        }
        binding.profileImg.setOnClickListener {
            singleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }

        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                intro = binding.introduce.text.toString()
                profile = profileUri?.toString()
                val currentData = openViewModel.sharedData.value ?: SharedOpenSyncData()
                currentData.syncIntro = intro
                currentData.image = profile
                if (currentData.syncType == "지속성") {
                    findNavController().navigate(R.id.action_openIntroductionFragment_to_openFirstFragment)
                } else
                    findNavController().navigate(R.id.action_openIntroductionFragment_to_openTimeFragment)
            }

        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun showPopup() {
        val popupLayoutBinding = PopupCancleSyncBinding.inflate(layoutInflater)
        val popupView = popupLayoutBinding.root

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(popupView)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        popupLayoutBinding.runBtn.setOnClickListener {
            alertDialog.dismiss() // 팝업 닫기
        }
        popupLayoutBinding.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
            // 현재 액티비티 종료
            requireActivity().finish()
        }

    }

    private fun setUpChangedListener() { // 이름에 값이 들어갈 때 다음 버튼 활성화
        binding.introduce.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.doneBtn.isEnabled = s?.isNotBlank() ?: false
                updateDoneButtonBackground()
                if (s.isNullOrEmpty()) {
                    binding.textLayout.setBackgroundResource(R.drawable.bg_edit_text)
                } else {
                    binding.textLayout.setBackgroundResource(R.drawable.label_white_primary)
                }
            }
        })
    }

    private fun updateDoneButtonBackground() {
        if (binding.doneBtn.isEnabled) { // 다음 버튼 스타일 변경
            binding.doneBtn.setTextColor(requireContext().resources.getColor(R.color.white))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_default)
        } else {
            binding.doneBtn.setTextColor(requireContext().resources.getColor(R.color.gray_70))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_gray_10)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }

    private fun hideKeyboard() {
        binding.introduce.clearFocus()
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}
