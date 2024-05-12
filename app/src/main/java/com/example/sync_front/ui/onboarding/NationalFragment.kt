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
import com.example.sync_front.api_server.CountriesManager
import com.example.sync_front.api_server.CountriesRequestModel
import com.example.sync_front.api_server.Onboarding
import com.example.sync_front.databinding.FragmentNationalBinding

class NationalFragment : Fragment() {
    lateinit var binding: FragmentNationalBinding
    private lateinit var adapter: SelectOneAdapter
    private lateinit var language: String
    private var profile: String ?= null
    private lateinit var name: String
    private lateinit var national: String
    private val args: NationalFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNationalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.doneBtn.isEnabled = false
        adapter = SelectOneAdapter(emptyList<String>())
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter

        // 전달된 데이터 읽기
        language = args.onboarding.language!!
        profile = args.onboarding.profile
        name = args.onboarding.userName!!

        Log.d("my log", "전달 받은값 $language $profile $name")

        updateSelectList()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                val action = NationalFragmentDirections.actionNationalFragmentToGenderFragment(
                    Onboarding(language, profile, name, "national", null, null, null, null)
                )
                findNavController().navigate(action)
            }
        }

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }

        // 어댑터 내에서 선택된 아이템이 변경될 때마다 다음 활성화 여부 업데이트
        adapter.setOnItemSelectListener { selected ->
            national = selected
            Log.d("my log", "선택 결과 - $national")
        }

        // 어댑터 내에서 선택된 아이템이 변경될 때마다 다음 활성화 여부 업데이트
        adapter.updateNextBtnListener { hasSelected ->
            Log.d("my log", "클릭됨 $hasSelected")
            binding.doneBtn.isEnabled = hasSelected
            binding.doneBtn.setTextColor(context!!.resources.getColor(R.color.white))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_default)
        }
    }

    private fun updateSelectList() { // 선택 리스트 출력
        val countriesRequest = CountriesRequestModel("1", "196", "korean")
        CountriesManager.getCountries(countriesRequest) { response ->
            response?.let {
                Log.d("my log", "국가 리스트 요청 성공")

                val countriesList: List<String> = ArrayList<String>().apply {
                    add("대한민국")
                    addAll(response)
                }.toList()

                adapter.updateData(countriesList)
            }
        }
    }
}