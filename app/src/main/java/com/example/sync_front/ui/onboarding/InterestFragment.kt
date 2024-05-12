package com.example.sync_front.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.databinding.FragmentInterestBinding
import com.example.sync_front.ui.main.my.SelectInterest
import com.example.sync_front.ui.main.my.SelectInterestAdapter

class InterestFragment : Fragment() {
    lateinit var binding: FragmentInterestBinding
    private lateinit var adapter: SelectInterestAdapter
    private lateinit var language: String
    private var profile: String ?= null
    private lateinit var name: String
    private lateinit var national: String
    private lateinit var gender: String
    private lateinit var univ: String
    private lateinit var type: String
    private val args: InterestFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInterestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSetting()
        updateSelectList()
        setupClickListeners()
    }

    private fun initSetting() {
        binding.doneBtn.isEnabled = false
        adapter = SelectInterestAdapter(emptyList<SelectInterest>())

        // 전달된 데이터 읽기
        language = args.onboarding.language!!
        profile = args.onboarding.profile
        name = args.onboarding.userName!!
        national = args.onboarding.countryName!!
        gender = args.onboarding.gender!!
        univ = args.onboarding.university!!
        type = args.onboarding.syncType!!
    }

    private fun setupClickListeners() {
        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                // 온보딩 요청 API 필요
                sendToServer()
                val action = InterestFragmentDirections.actionInterestFragmentToOnboardingDoneFragment()
                findNavController().navigate(action)
            }
        }

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }
    }

    private fun sendToServer() { // 온보딩 요청

    }

    private fun updateSelectList() { // 관심사 선택 리스트 출력
        val interest1 = SelectInterest("@drawable/ic_exchange_language", "외국어", listOf("언어 교환", "튜터링", "스터디"))
        val interest2 = SelectInterest("@drawable/ic_culture", "문화·예술", listOf("문학·예술", "영화", "드라마", "미술·디자인", "공연·전시", "음악"))
        val interest3 = SelectInterest("@drawable/ic_travel", "여행·동행", listOf("관광지", "자연", "휴양"))
        val interest4 = SelectInterest("@drawable/ic_travel", "액티비티", listOf("러닝·산책", "등산", "클라이밍", "자전거", "축구", "서핑", "테니스", "볼링", "탁구"))
        val interest5 = SelectInterest("@drawable/ic_food", "푸드·음료", listOf("맛집", "카페", "술"))
        val interest6 = SelectInterest("@drawable/ic_etc", "기타", listOf("기타"))

        val interestList = listOf(interest1, interest2, interest3, interest4, interest5, interest6)
        adapter = SelectInterestAdapter(interestList)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
    }
}