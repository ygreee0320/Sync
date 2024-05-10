package com.example.sync_front.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentNationalBinding

class NationalFragment : Fragment() {
    lateinit var binding: FragmentNationalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNationalBinding.inflate(inflater, container, false)
//
//        binding.doneBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_languageFragment_to_ProfileFragment)
//        }
//
//        binding.cancelBtn.setOnClickListener {
//        }

        return binding.root
    }
}