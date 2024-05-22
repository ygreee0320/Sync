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
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.data.model.SharedOpenSyncData
import com.example.sync_front.databinding.FragmentOpenTitleBinding

class OpenTitleFragment : Fragment() {
    private var _binding: FragmentOpenTitleBinding? = null
    private val binding get() = _binding!!
    private val openViewModel: OpenViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setUpChangedListener()
        setupClickListeners()
    }
    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d("OpenTitleFragment", "Received syncType: ${data.syncType}")
            Log.d("OpenTitleFragment", "Received syncName: ${data.syncName}")
            Log.d("OpenTitleFragment", "Received detailType: ${data.detailType}")
            // 데이터를 기반으로 UI 업데이트나 다른 로직 수행
        }
    }
    private fun setupClickListeners() {
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }

        binding.doneBtn.setOnClickListener {
            val currentData = openViewModel.sharedData.value ?: SharedOpenSyncData()
            currentData.syncName = binding.title.text.toString()
            openViewModel.updateData(currentData)
            findNavController().navigate(R.id.action_openTitleFragment_to_openIntroductionFragment)
        }

        binding.textCancel.setOnClickListener {
            binding.title.setText("") // 텍스트 초기화
        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun setUpChangedListener() { //값이 들어갈 때 다음 버튼 활성화
        binding.title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.doneBtn.isEnabled = s?.isNotBlank() ?: false
                updateDoneButtonBackground()

                if (s.isNullOrEmpty()) {
                    binding.textCancel.visibility = View.GONE
                    binding.textLayout.setBackgroundResource(R.drawable.bg_edit_text)
                } else {
                    binding.textCancel.visibility = View.VISIBLE
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
        binding.title.clearFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}