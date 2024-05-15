package com.example.sync_front.ui.open

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenTimeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OpenTimeFragment : Fragment() {
    private var _binding: FragmentOpenTimeBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDatePickerDialog()
        initializeTimePickerDialog()
        nextButton()
    }

    private fun initializeDatePickerDialog() {
        val calendar = Calendar.getInstance()
        datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // 날짜 객체를 생성합니다.
                val date = Calendar.getInstance()
                date.set(year, month, dayOfMonth)

                // 날짜와 요일을 함께 표시하는 포맷을 설정합니다.
                val dateFormat = SimpleDateFormat("M월 d일 (E)", Locale.KOREA)
                val formattedDate = dateFormat.format(date.time)

                // 텍스트 뷰에 날짜를 설정합니다.
                binding.dateText.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // 날짜 선택 박스에 클릭 리스너를 설정합니다.
        binding.boxDate.setOnClickListener {
            datePickerDialog.show()
        }
    }
    private fun initializeTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        timePickerDialog = TimePickerDialog(context, { _, selectedHour, selectedMinute ->
            // 캘린더 객체에 선택된 시간을 설정
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)

            // "a h:mm" 포맷으로 시간 형식 지정 (예: 오후 11:30)
            val format = SimpleDateFormat("a h:mm", Locale.getDefault())
            val formattedTime = format.format(calendar.time)

            // 선택된 시간을 TextView에 표시
            binding.timeText.text = formattedTime
        }, hour, minute, false) // false로 설정하여 12시간제로 표시

        binding.boxTime.setOnClickListener {
            timePickerDialog.show()
        }
    }
    private fun nextButton() {
        binding.doneBtn.setOnClickListener {
            findNavController().navigate(R.id.action_openTimeFragment_to_openLocationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}