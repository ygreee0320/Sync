package com.example.sync_front.ui.open

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.data.model.SharedOpenSyncData
import com.example.sync_front.databinding.FragmentOpenTypeBinding
import com.example.sync_front.databinding.PopupCancleSyncBinding
import android.graphics.Color


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
        binding.toolbar.setNavigationOnClickListener {
            showPopup()
        }
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
                Log.d(
                    "OpenTypeFragment",
                    "Received sync type: ${openViewModel.sharedData}"
                )
                findNavController().navigate(R.id.action_openTypeFragment_to_openThemeFragment)
            }
        }
    }

    private fun showPopup() {
        val popupLayoutBinding = PopupCancleSyncBinding.inflate(layoutInflater)
        val popupView = popupLayoutBinding.root

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(popupView)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        popupLayoutBinding.runBtn.setOnClickListener {
            alertDialog.dismiss() // 팝업 닫기
        }
        popupLayoutBinding.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
            // 현재 액티비티 종료
            requireActivity().finish()
        }

    }

    private fun updateSyncType() {
        val newSyncType = when {
            binding.boxOnetime.isSelected -> "일회성"
            binding.boxPersistence.isSelected -> "지속성"
            binding.boxFriend.isSelected -> "내친소"
            else -> ""
        }
        Log.d("OpenTypeFragment", "Updating sync type: $newSyncType")

        val currentData = openViewModel.sharedData.value ?: SharedOpenSyncData()
        currentData.syncType = newSyncType
        openViewModel.updateData(currentData)
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