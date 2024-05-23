package com.example.sync_front.ui.open

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.graphics.Color
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenLeaderBinding
import com.example.sync_front.data.model.SharedOpenSyncData
import com.example.sync_front.databinding.PopupCancleSyncBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard

class OpenLeaderFragment : Fragment() {
    private var _binding: FragmentOpenLeaderBinding? = null
    private val binding get() = _binding!!
    private val openViewModel: OpenViewModel by activityViewModels()
    private var profileUri: Uri? = null  // 프로필 uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenLeaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setUpChangedListener()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }
        binding.toolbar.setNavigationOnClickListener {
            showPopup()
        }
        binding.doneBtn.setOnClickListener {
            val currentData = openViewModel.sharedData.value ?: SharedOpenSyncData()
            currentData.userIntro = binding.introduce.text.toString()
            openViewModel.updateData(currentData)
            findNavController().navigate(R.id.action_openLeaderFragment_to_openPreviewFragment)
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
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d(javaClass.simpleName, "Received data: $data")
        }
        profileUri = openViewModel.sharedData.value?.image.toString().toUri()
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
        _binding = null
    }

    private fun hideKeyboard() {
        binding.introduce.clearFocus()
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}
