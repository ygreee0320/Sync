package com.example.sync_front.ui.onboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentLanguageBinding

class LanguageFragment : Fragment() {
    lateinit var binding: FragmentLanguageBinding
    private lateinit var adapter: SelectOneAdapter
    private lateinit var language: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.doneBtn.isEnabled = false

        updateSelectList()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                val action =
                    LanguageFragmentDirections.actionLanguageFragmentToProfileFragment(language)
                findNavController().navigate(action)
            }
        }

        binding.beforeBtn.setOnClickListener {
            requireActivity().finish() // OnboardingActivity 종료
        }

        // 어댑터 내에서 선택된 아이템이 변경될 때마다 다음 활성화 여부 업데이트
        adapter.setOnItemSelectListener { selected ->
            language = selected
            Log.d("my log", "선택 결과 - $language")
        }

        // 어댑터 내에서 선택된 아이템이 변경될 때마다 다음 활성화 여부 업데이트
        adapter.updateNextBtnListener { hasSelected ->
            binding.doneBtn.isEnabled = hasSelected
            binding.doneBtn.setTextColor(context!!.resources.getColor(R.color.white))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_default)
        }
    }

    private fun updateSelectList() { // 선택 리스트 출력
        val selectList = listOf<String>("한국어", "English")

        adapter = SelectOneAdapter(selectList)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
    }
}