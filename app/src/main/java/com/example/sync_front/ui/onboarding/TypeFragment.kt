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
import com.example.sync_front.api_server.Onboarding
import com.example.sync_front.databinding.FragmentTypeBinding

class TypeFragment : Fragment() {
    lateinit var binding: FragmentTypeBinding
    private lateinit var adapter: SelectOneAdapter
    private lateinit var language: String
    private var profile: String ?= null
    private lateinit var name: String
    private lateinit var national: String
    private lateinit var gender: String
    private lateinit var univ: String
    private lateinit var type: String
    private val args: TypeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTypeBinding.inflate(inflater, container, false)
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

        // 전달된 데이터 읽기
        language = args.onboarding.language!!
        profile = args.onboarding.profile
        name = args.onboarding.userName!!
        national = args.onboarding.countryName!!
        gender = args.onboarding.gender!!
        univ = args.onboarding.university!!

        binding.explainName.setText(name)
        binding.explainDetailName.setText(name)
    }

    private fun setupClickListeners() {
        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                val action = TypeFragmentDirections.actionTypeFragmentToInterestFragment(
                    Onboarding(language, profile, name, national, gender, univ, type, null)
                )
                findNavController().navigate(action)
            }
        }

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }

        // 어댑터 내에서 선택된 아이템이 변경될 때마다 다음 활성화 여부 업데이트
        adapter.setOnItemSelectListener { selected ->
            type = selected
            Log.d("my log", "선택 결과 - $type")
        }

        // 어댑터 내에서 선택된 아이템이 변경될 때마다 다음 활성화 여부 업데이트
        adapter.updateNextBtnListener { hasSelected ->
            binding.doneBtn.isEnabled = hasSelected
            binding.doneBtn.setTextColor(context!!.resources.getColor(R.color.white))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_default)
        }
    }

    private fun updateSelectList() { // 선택 리스트 출력
        val selectList = listOf<String>(
            context!!.getString(R.string.성향1),
            context!!.getString(R.string.성향2),
            context!!.getString(R.string.성향3))

        adapter = SelectOneAdapter(selectList)
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
    }
}