package com.example.sync_front.ui.open

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenFirstBinding
import com.example.sync_front.databinding.FragmentOpenTypeBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class OpenFirstFragment : Fragment() {
    private var _binding: FragmentOpenFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private var dateSelected = false
    private var timeSelected = false
    private val openViewModel: OpenViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDatePickerDialog()
        initializeTimePickerDialog()
        observeViewModel()
        setupClickListeners()

    }

    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d("OpenFirstFragment", "Received sync type: ${data.syncType}")
            Log.d("OpenFirstFragment", "Received sync type: ${data.syncName}")
            Log.d("OpenFirstFragment", "Received sync type: ${data.image}")
            // 데이터를 기반으로 UI 업데이트나 다른 로직 수행
        }
    }
    private fun initializeDatePickerDialog() {
        val calendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            { _, year, month, dayOfMonth ->
                // 날짜 객체를 생성합니다.
                val date = Calendar.getInstance()
                date.set(year, month, dayOfMonth)

                // 날짜와 요일을 함께 표시하는 포맷을 설정합니다.
                val dateFormat = SimpleDateFormat("M월 d일 (E)", Locale.KOREA)
                val formattedDate = dateFormat.format(date.time)

                // 텍스트 뷰에 날짜를 설정합니다.
                binding.dateText.text = formattedDate
                dateSelected = true
                checkIfDateTimeSelected()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)  // 배경 투명 처리

        // 날짜 선택 박스에 클릭 리스너
        binding.boxDate.setOnClickListener {
            datePickerDialog.show()
        }
    }
    private fun initializeTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        timePickerDialog = TimePickerDialog(
            context,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            { _, selectedHour, selectedMinute ->
                // 캘린더 객체에 선택된 시간을 설정
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                // "a h:mm" 포맷으로 시간 형식 지정
                val format = SimpleDateFormat("a h:mm", Locale.getDefault())
                val formattedTime = format.format(calendar.time)

                // 선택된 시간을 TextView에 표시
                binding.timeText.text = formattedTime
                timeSelected = true
                checkIfDateTimeSelected()


            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)  // 배경 투명 처리

        binding.boxTime.setOnClickListener {
            timePickerDialog.show()
        }
    }

    private fun checkIfDateTimeSelected() {
        if (dateSelected) {
            binding.boxDate.setBackgroundResource(R.drawable.label_white_primary)
        }
        if (timeSelected) {
            binding.boxTime.setBackgroundResource(R.drawable.label_white_primary)
        }
        binding.doneBtn.isEnabled = dateSelected && timeSelected
        if (binding.doneBtn.isEnabled) {
            binding.doneBtn.setTextColor(resources.getColor(R.color.white))  // 활성화 시 텍스트 색상 변경
            binding.doneBtn.setBackgroundResource(R.drawable.btn_default)
        } else {
            binding.doneBtn.setTextColor(resources.getColor(R.color.gray_70))  // 비활성화 시 텍스트 색상 변경
        }
    }

    private fun setupClickListeners() {
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }
        binding.doneBtn.setOnClickListener {
            findNavController().navigate(R.id.action_openFirstFragment_to_openRoutineFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}