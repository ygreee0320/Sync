package com.example.sync_front.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.sync_front.databinding.FragmentOnboardingDoneBinding
import com.example.sync_front.ui.main.MainActivity

class OnboardingDoneFragment : Fragment() {
    lateinit var binding: FragmentOnboardingDoneBinding
    private lateinit var name: String
    private val args: OnboardingDoneFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSetting()
        setupClickListeners()
    }

    private fun initSetting() {
        name = args.name
        binding.explainName.setText(name)
        binding.explainDetail2Name.setText(name)
    }

    private fun setupClickListeners() {
        binding.doneBtn.setOnClickListener { // 싱크 시작하기 클릭 시 온보딩 종료 후 메인으로 이동
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }
}