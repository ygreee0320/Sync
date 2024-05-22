package com.example.sync_front.ui.open

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenThemeBinding
import android.util.Log
import com.example.sync_front.data.model.SharedOpenSyncData


class OpenThemeFragment : Fragment() {
    private var _binding: FragmentOpenThemeBinding? = null
    private val binding get() = _binding!!
    private val openViewModel: OpenViewModel by activityViewModels()

    private lateinit var adapter: SelectThemeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
        initSetting()
        updateSelectList()
    }

    private fun initSetting() {
        binding.doneBtn.isEnabled = false
        adapter = SelectThemeAdapter(emptyList<SelectTheme>()) {}
    }

    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d(javaClass.simpleName, "Received data: $data")
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

    private fun updateSelectList() { // 관심사 선택 리스트 출력
        val interest1 = SelectTheme("@drawable/ic_exchange_language", getString(R.string.foreignLanguage),
            listOf(getString(R.string.languageExchange), getString(R.string.tutoring), getString(R.string.study)))
        val interest2 = SelectTheme("@drawable/ic_culture", getString(R.string.cultureArt),
            listOf(getString(R.string.movie), getString(R.string.drama), getString(R.string.art), getString(R.string.performance), getString(R.string.music)))
        val interest3 = SelectTheme("@drawable/ic_travel", getString(R.string.travelCompanion),
            listOf(getString(R.string.sightseeing), getString(R.string.nature), getString(R.string.vacation)))
        val interest4 = SelectTheme("@drawable/ic_activity", getString(R.string.activity),
            listOf(getString(R.string.running), getString(R.string.hiking), getString(R.string.climbing),
                getString(R.string.bike), getString(R.string.soccer), getString(R.string.surfing),
                getString(R.string.tennis), getString(R.string.bowling), getString(R.string.tableTennis)))
        val interest5 = SelectTheme("@drawable/ic_food", getString(R.string.foodAndDrink),
            listOf(getString(R.string.restaurant), getString(R.string.cafe), getString(R.string.drink)))
        val interest6 = SelectTheme("@drawable/ic_etc", getString(R.string.etc),
            listOf(getString(R.string.etc)))

        val interestList = listOf(interest1, interest2, interest3, interest4, interest5, interest6)

        val themeList = listOf(interest1, interest2, interest3, interest4, interest5, interest6)
        adapter = SelectThemeAdapter(themeList) { enable ->
            binding.doneBtn.isEnabled = enable
            updateDoneButtonBackground()
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }
        binding.doneBtn.setOnClickListener {
            val clickedItems = adapter.getClickedItems()
            Log.d("my log", "선택된 관심사- $clickedItems")

            val currentData = openViewModel.sharedData.value ?: SharedOpenSyncData()
            currentData.detailType = clickedItems
            openViewModel.updateData(currentData)

            findNavController().navigate(R.id.action_openThemeFragment_to_openTitleFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}