package com.example.sync_front.ui.open

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenTypeBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard


class OpenTypeFragment : Fragment() {
    private var _binding: FragmentOpenTypeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.boxOnetime.setOnClickListener {
            it.isSelected = true
            binding.boxPersistence.isSelected = false
            binding.boxFriend.isSelected = false
            checkNextButtonState()
        }
        binding.boxPersistence.setOnClickListener {
            it.isSelected = true
            binding.boxOnetime.isSelected = false
            binding.boxFriend.isSelected = false
            checkNextButtonState ()
        }
        binding.boxFriend.setOnClickListener {
            it.isSelected = true
            binding.boxOnetime.isSelected = false
            binding.boxPersistence.isSelected = false
            checkNextButtonState ()
        }
    }

    private fun checkNextButtonState() {
        val isAnySubscribeSelected =
            listOf(binding.boxOnetime, binding.boxPersistence, binding.boxFriend).any { it.isSelected }

        binding.doneBtn.isEnabled = isAnySubscribeSelected
        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled)
                nextButton()
        }
    }

    private fun nextButton() {
        binding.doneBtn.setOnClickListener {
            findNavController().navigate(R.id.action_openTypeFragment_to_openThemeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }

}