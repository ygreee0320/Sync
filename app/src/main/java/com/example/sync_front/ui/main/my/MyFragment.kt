package com.example.sync_front.ui.main.my

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentMyBinding

class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.reviewBtn.setOnClickListener {
            //val intent = Intent(context, SyncActivity::class.java)
            //startActivity(intent)
        }
        binding.bookmarkBtn.setOnClickListener {
            val intent = Intent(context, BookmarkActivity::class.java)
            startActivity(intent)
        }
        binding.questionBtn.setOnClickListener {
            //val intent = Intent(context, SyncActivity::class.java)
            //startActivity(intent)
        }
        binding.mySync1.setOnClickListener { // 내가 개설한 싱크 목록
            binding.mySync1Txt.setTextColor(Color.WHITE)
            binding.mySync2Txt.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
            binding.mySync1.setBackgroundResource(R.drawable.bg_btn_mypage_left_clicked)
            binding.mySync2.setBackgroundResource(R.drawable.bg_btn_mypage_right)
        }

        binding.mySync2.setOnClickListener { // 내가 참여한 싱크 목록
            binding.mySync2Txt.setTextColor(Color.WHITE)
            binding.mySync1Txt.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
            binding.mySync1.setBackgroundResource(R.drawable.bg_btn_mypage_left)
            binding.mySync2.setBackgroundResource(R.drawable.bg_btn_mypage_right_clicked)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}