package com.example.sync_front.ui.open

import android.app.TimePickerDialog
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenRoutineBinding
import com.example.sync_front.databinding.PopupCancleSyncBinding
import androidx.appcompat.app.AlertDialog


class OpenRoutineFragment : Fragment() {
    private var _binding: FragmentOpenRoutineBinding? = null
    private val binding get() = _binding!!
    private val openViewModel: OpenViewModel by activityViewModels()
    private var selectedTextView: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenRoutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSetting()
        setupClickListeners()
        setupTimePicker()
        observeViewModel()
        startEntryAnimations()
    }

    private fun initSetting() {
        //   binding.doneBtn.isEnabled = false
    }

    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d(javaClass.simpleName, "Received data: $data")
        }
    }

    private fun updateDoneButtonBackground() {
        val regularDay = openViewModel.sharedData.value?.regularDay
        val regularTime = openViewModel.sharedData.value?.regularTime

        if (!regularDay.isNullOrEmpty() && !regularTime.isNullOrEmpty()) {
            // 요일과 시간이 모두 선택된 경우
            binding.doneBtn.isEnabled = true
            binding.doneBtn.setTextColor(requireContext().resources.getColor(R.color.white))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_default)
        } else {
            // 요일 또는 시간 중 하나라도 선택되지 않은 경우
            binding.doneBtn.isEnabled = false
            binding.doneBtn.setTextColor(requireContext().resources.getColor(R.color.gray_70))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_gray_10)
        }
    }

    private fun toggleTextViewSelection(textView: TextView) {
        if (selectedTextView == textView) {
            // 이미 선택된 TextView를 클릭한 경우
            textView.isSelected = false
            selectedTextView = null
        } else {
            // 새로운 TextView를 클릭한 경우
            selectedTextView?.isSelected = false // 이전에 선택된 TextView의 선택 해제
            textView.isSelected = true // 선택된 TextView로 설정
            selectedTextView = textView

            // 선택된 요일을 ViewModel에 저장
            openViewModel.sharedData.value?.regularDay = textView.text.toString()

            // 요일과 시간이 모두 선택되면 "완료" 버튼 활성화
            updateDoneButtonBackground()
        }
    }

    private fun setupTimePicker() {
        binding.timeText.setOnClickListener {
            // 시간을 선택할 때 이벤트 처리
            val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                // 시간을 형식에 맞게 변환하여 TextView에 표시
                val timeString = String.format("%02d:%02d", hourOfDay, minute)
                binding.timeText.text = timeString

                // 선택된 시간을 ViewModel에 저장
                openViewModel.sharedData.value?.regularTime = timeString

                // 요일과 시간이 모두 선택되면 "완료" 버튼 활성화
                updateDoneButtonBackground()
            }, 0, 0, false)

            timePickerDialog.show()
        }
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            showPopup()
        }
        binding.dateText1.setOnClickListener { toggleTextViewSelection(binding.dateText1) }
        binding.dateText2.setOnClickListener { toggleTextViewSelection(binding.dateText2) }
        binding.dateText3.setOnClickListener { toggleTextViewSelection(binding.dateText3) }
        binding.dateText4.setOnClickListener { toggleTextViewSelection(binding.dateText4) }
        binding.dateText5.setOnClickListener { toggleTextViewSelection(binding.dateText5) }
        binding.dateText6.setOnClickListener { toggleTextViewSelection(binding.dateText6) }
        binding.dateText7.setOnClickListener { toggleTextViewSelection(binding.dateText7) }
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }
        binding.doneBtn.setOnClickListener {
            findNavController().navigate(R.id.action_openRoutineFragment_to_openLocationFragment)
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

        binding.explain3.apply {
            translationY = initialTranslationY
            alpha = 0f
            animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(interpolator)
                .setDuration(duration)
                .start()
        }

        binding.boxDate.apply {
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

        binding.explain4.apply {
            translationY = initialTranslationY
            alpha = 0f
            animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(interpolator)
                .setDuration(duration)
                .setStartDelay(400L)
                .start()
        }

        binding.boxTime.apply {
            translationY = initialTranslationY
            alpha = 0f
            animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(interpolator)
                .setDuration(duration)
                .setStartDelay(600L)
                .start()
        }
    }
}