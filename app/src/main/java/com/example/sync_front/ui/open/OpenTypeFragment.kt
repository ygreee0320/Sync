package com.example.sync_front.ui.open

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenTypeBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard


class OpenTypeFragment : Fragment() {
    private var _binding: FragmentOpenTypeBinding? = null
    private val binding get() = _binding!!
    private val openViewModel: OpenViewModel by activityViewModels()

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
        binding.beforeBtn.setOnClickListener {
            activity?.finish()
        }
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
            checkNextButtonState()
        }
        binding.boxFriend.setOnClickListener {
            it.isSelected = true
            binding.boxOnetime.isSelected = false
            binding.boxPersistence.isSelected = false
            checkNextButtonState()
        }

        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                updateSyncType()
                findNavController().navigate(R.id.action_openTypeFragment_to_openThemeFragment)
            }
        }
    }

    private fun updateSyncType() {
        val newSyncType = when {
            binding.boxOnetime.isSelected -> "일회성"
            binding.boxPersistence.isSelected -> "지속성"
            binding.boxFriend.isSelected -> "내친소"
            else -> ""
        }

        // ViewModel에 있는 sharedData의 현재 값에 접근하여 업데이트
        openViewModel.sharedData.value?.let {
            it.syncType = newSyncType
            openViewModel.updateData(it)
        }
    }

    private fun checkNextButtonState() {
        val isAnySubscribeSelected = listOf(
            binding.boxOnetime,
            binding.boxPersistence,
            binding.boxFriend
        ).any { it.isSelected }

        binding.doneBtn.isEnabled = isAnySubscribeSelected

        if (isAnySubscribeSelected) {
            binding.doneBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.white
                )
            )
        } else {
            binding.doneBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_70))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }

}