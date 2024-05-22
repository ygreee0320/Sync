package com.example.sync_front.ui.open

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenRoutineBinding


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
        observeViewModel()
    }
    private fun initSetting() {
     //   binding.doneBtn.isEnabled = false
    }
    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d("OpenRoutineFragment", "Received syncType: ${data.syncType}")
        }
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
        }
    }

    private fun setupClickListeners() {
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
}