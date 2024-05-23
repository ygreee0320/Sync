package com.example.sync_front.ui.open

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenCntBinding
import androidx.navigation.fragment.findNavController
import com.example.sync_front.data.model.SharedOpenSyncData
import com.example.sync_front.databinding.PopupCancleSyncBinding
import androidx.appcompat.app.AlertDialog


class OpenCntFragment : Fragment() {
    private var _binding: FragmentOpenCntBinding? = null
    private val binding get() = _binding!!
    private val openViewModel: OpenViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenCntBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpChangedListener()
        observeViewModel()
        setupClickListeners()
        startEntryAnimations()
    }

    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d(javaClass.simpleName, "Received data: $data")
        }
    }

    private fun setUpChangedListener() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // 두 입력 필드 모두 비어있지 않은지 확인
                val minFilled = binding.minCnt.text?.isNotBlank() ?: false
                val maxFilled = binding.maxCnt.text?.isNotBlank() ?: false
                binding.doneBtn.isEnabled = minFilled && maxFilled
                updateDoneButtonBackground()

                // 각 입력 필드의 배경 업데이트
                updateInputFieldBackground(binding.minCnt.text, binding.textLayout1)
                updateInputFieldBackground(binding.maxCnt.text, binding.textLayout2)
            }
        }

        // 두 EditText에 동일한 TextWatcher 설정
        binding.minCnt.addTextChangedListener(textWatcher)
        binding.maxCnt.addTextChangedListener(textWatcher)
    }

    private fun updateInputFieldBackground(text: Editable?, textLayout: View) {
        if (text.isNullOrEmpty()) {
            textLayout.setBackgroundResource(R.drawable.bg_edit_text)
        } else {
            textLayout.setBackgroundResource(R.drawable.label_white_primary)
        }
    }

    private fun updateDoneButtonBackground() {
        if (binding.doneBtn.isEnabled) {
            binding.doneBtn.setTextColor(requireContext().resources.getColor(R.color.white))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_default)
        } else {
            binding.doneBtn.setTextColor(requireContext().resources.getColor(R.color.gray_70))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_gray_10)
        }
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            showPopup()
        }
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }
        binding.doneBtn.setOnClickListener {
            val currentData = openViewModel.sharedData.value ?: SharedOpenSyncData()
            currentData.member_min = binding.minCnt.text.toString().toInt()
            currentData.member_max = binding.maxCnt.text.toString().toInt()
            openViewModel.updateData(currentData)
            findNavController().navigate(R.id.action_openCntFragment_to_openLeaderFragment)
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

    private fun startEntryAnimations() {
        val initialTranslationY = 300f
        val duration = 1000L
        val interpolator = AccelerateDecelerateInterpolator()

        binding.tv1.apply {
            translationY = initialTranslationY
            alpha = 0f
            animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(interpolator)
                .setDuration(duration)
                .start()
        }

        binding.tv2.apply {
            translationY = initialTranslationY
            alpha = 0f
            animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(interpolator)
                .setDuration(duration)
                .setStartDelay(200L)
                .start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }

    private fun hideKeyboard() {
        binding.minCnt.clearFocus()
        binding.maxCnt.clearFocus()
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}