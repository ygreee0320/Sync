package com.example.sync_front.ui.open

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.graphics.Color
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenLeaderBinding
import com.example.sync_front.data.model.SharedOpenSyncData
import com.example.sync_front.databinding.PopupCancleSyncBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard

class OpenLeaderFragment : Fragment() {
    private var _binding: FragmentOpenLeaderBinding? = null
    private val binding get() = _binding!!
    private val openViewModel: OpenViewModel by activityViewModels()
    private var profileUri: Uri? = null  // 프로필 uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenLeaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        hideKeyboard()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }
        binding.toolbar.setNavigationOnClickListener {
            showPopup()
        }
        binding.doneBtn.setOnClickListener {
            val currentData = openViewModel.sharedData.value ?: SharedOpenSyncData()
            currentData.userIntro = binding.introduce.text.toString()
            openViewModel.updateData(currentData)
            findNavController().navigate(R.id.action_openLeaderFragment_to_openPreviewFragment)
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

    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d(javaClass.simpleName, "Received data: $data")
        }
        profileUri = openViewModel.sharedData.value?.image.toString().toUri()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideKeyboard() {
        binding.introduce.clearFocus()
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}
