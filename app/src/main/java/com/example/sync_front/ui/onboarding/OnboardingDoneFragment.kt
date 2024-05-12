package com.example.sync_front.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sync_front.databinding.FragmentOnboardingDoneBinding

class OnboardingDoneFragment : Fragment() {
    lateinit var binding: FragmentOnboardingDoneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.doneBtn.setOnClickListener { // 싱크 시작하기 클릭 시
            // 온보딩 api연결 후 메인 액티비티로 이동
        }
    }

}