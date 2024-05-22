package com.example.sync_front.ui.open

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenLocationBinding
import androidx.navigation.fragment.findNavController
import com.example.sync_front.data.model.SharedOpenSyncData

class OpenLocationFragment : Fragment() {
    private var _binding: FragmentOpenLocationBinding? = null
    private val binding get() = _binding!!
    private val openViewModel: OpenViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenLocationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
        setUpChangedListener()
    }
    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d("OpenLocationFragment", "Received syncType: ${data.syncType}")
            Log.d("OpenLocationFragment", "Received syncName: ${data.syncName}")
            Log.d("OpenLocationFragment", "Received image: ${data.image}")
            Log.d("OpenLocationFragment", "Received syncIntro: ${data.syncIntro}")
            Log.d("OpenLocationFragment", "Received date: ${data.date}")
            Log.d("OpenLocationFragment", "Received regularDay: ${data.regularDay}")
            Log.d("OpenLocationFragment", "Received regularTime: ${data.regularTime}")
            // 데이터를 기반으로 UI 업데이트나 다른 로직 수행
        }
    }
    private fun setupClickListeners() {
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }
        binding.doneBtn.setOnClickListener {
            val currentData = openViewModel.sharedData.value ?: SharedOpenSyncData()
            currentData.location = binding.location.text.toString()
            openViewModel.updateData(currentData)
            findNavController().navigate(R.id.action_openLocationFragment_to_openCntFragment)
        }
        binding.root.setOnClickListener {
            hideKeyboard()
        }
    }


    private fun setUpChangedListener() { // 이름에 값이 들어갈 때 다음 버튼 활성화
        binding.location.addTextChangedListener(object : TextWatcher {
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
        binding.location.clearFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}