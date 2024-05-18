package com.example.sync_front.ui.onboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.R
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
        adapter = SelectInterestAdapter(emptyList<SelectInterest>()){}

        // 전달된 데이터 읽기
        language = args.onboarding.language!!
        profile = args.onboarding.profile
        name = args.onboarding.userName!!
        national = args.onboarding.countryName!!
        gender = args.onboarding.gender!!
        univ = args.onboarding.university!!
        type = args.onboarding.syncType!!

        binding.explainName.setText(name)
        binding.explainDetailName.setText(name)
    }

    private fun setupClickListeners() {
        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                // 온보딩 요청 API 필요
                sendToServer()
                val action = InterestFragmentDirections.actionInterestFragmentToOnboardingDoneFragment(name)
                findNavController().navigate(action)
            }
        }

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }
    }

    private fun sendToServer() { // 온보딩 요청
        val clickedItems = adapter.getClickedItems()
        Log.d("my log", "선택된 관심사- $clickedItems")


    }

    private fun updateDoneButtonBackground() {
        if (binding.doneBtn.isEnabled) { // 다음 버튼 스타일 변경
            binding.doneBtn.setTextColor(context!!.resources.getColor(R.color.white))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_default)
        } else {
            binding.doneBtn.setTextColor(context!!.resources.getColor(R.color.gray_70))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_gray_10)
        }
    }

    private fun updateSelectList() { // 관심사 선택 리스트 출력
        val interest1 = SelectInterest("@drawable/ic_exchange_language", getString(R.string.foreignLanguage),
            listOf(getString(R.string.languageExchange), getString(R.string.tutoring), getString(R.string.study)))
        val interest2 = SelectInterest("@drawable/ic_culture", getString(R.string.cultureArt),
            listOf(getString(R.string.movie), getString(R.string.drama), getString(R.string.art), getString(R.string.performance), getString(R.string.music)))
        val interest3 = SelectInterest("@drawable/ic_travel", getString(R.string.travelCompanion),
            listOf(getString(R.string.sightseeing), getString(R.string.nature), getString(R.string.vacation)))
        val interest4 = SelectInterest("@drawable/ic_activity", getString(R.string.activity),
            listOf(getString(R.string.running), getString(R.string.hiking), getString(R.string.climbing),
                getString(R.string.bike), getString(R.string.soccer), getString(R.string.surfing),
                getString(R.string.tennis), getString(R.string.bowling), getString(R.string.tableTennis)))
        val interest5 = SelectInterest("@drawable/ic_food", getString(R.string.foodAndDrink),
            listOf(getString(R.string.restaurant), getString(R.string.cafe), getString(R.string.drink)))
        val interest6 = SelectInterest("@drawable/ic_etc", getString(R.string.etc),
            listOf(getString(R.string.etc)))

        val interestList = listOf(interest1, interest2, interest3, interest4, interest5, interest6)
        adapter = SelectInterestAdapter(interestList) { enable ->
            binding.doneBtn.isEnabled = enable
            updateDoneButtonBackground()
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
    }
}