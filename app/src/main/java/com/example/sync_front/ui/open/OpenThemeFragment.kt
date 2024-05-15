package com.example.sync_front.ui.open

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenThemeBinding


class OpenThemeFragment : Fragment() {
    private var _binding: FragmentOpenThemeBinding? = null
    private val binding get() = _binding!!
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
        nextButton()
        initSetting()
        updateSelectList()
    }

    private fun initSetting() {
        adapter = SelectThemeAdapter(emptyList<SelectTheme>())

    }

    private fun updateSelectList() { // 관심사 선택 리스트 출력
        val interest1 =
            SelectTheme("@drawable/ic_exchange_language", "외국어", listOf("언어 교환", "튜터링", "스터디"))
        val interest2 = SelectTheme(
            "@drawable/ic_culture",
            "문화·예술",
            listOf("문학·예술", "영화", "드라마", "미술·디자인", "공연·전시", "음악")
        )
        val interest3 = SelectTheme("@drawable/ic_travel", "여행·동행", listOf("관광지", "자연", "휴양"))
        val interest4 = SelectTheme(
            "@drawable/ic_travel",
            "액티비티",
            listOf("러닝·산책", "등산", "클라이밍", "자전거", "축구", "서핑", "테니스", "볼링", "탁구")
        )
        val interest5 = SelectTheme("@drawable/ic_food", "푸드·음료", listOf("맛집", "카페", "술"))
        val interest6 = SelectTheme("@drawable/ic_etc", "기타", listOf("기타"))

        val themeList = listOf(interest1, interest2, interest3, interest4, interest5, interest6)
        adapter = SelectThemeAdapter(themeList)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
    }
    private fun nextButton(){
        binding.doneBtn.setOnClickListener {
            findNavController().navigate(R.id.action_openThemeFragment_to_openTitleFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}