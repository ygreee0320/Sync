package com.example.sync_front.ui.open

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenCntBinding
import androidx.navigation.fragment.findNavController


class OpenCntFragment : Fragment() {
    private var _binding: FragmentOpenCntBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenCntBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextButton()
    }
    private fun nextButton() {
        binding.doneBtn.setOnClickListener {
            findNavController().navigate(R.id.action_openCntFragment_to_openLeaderFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}