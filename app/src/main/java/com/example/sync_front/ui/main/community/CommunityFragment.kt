package com.example.sync_front.ui.main.community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.api_server.CommunityManager
import com.example.sync_front.data.model.Community
import com.example.sync_front.databinding.FragmentCommunityBinding
import com.google.android.material.tabs.TabLayout

class CommunityFragment : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    private lateinit var communityList: List<Community>
    private lateinit var adapter: CommunityAdapter
    private var authToken: String ?= null // 로그인 토큰

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialSetting()
        setTabLayout()
        setupClickListeners()
    }

    private fun setTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        getCommunity("생활")
                    }
                    1 -> {
                        getCommunity("질문")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun initialSetting() {
        // 저장된 토큰 읽어오기
        val sharedPreferences = requireActivity().getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        Log.d("my log", "현재 토큰 값: $authToken")

        adapter = CommunityAdapter(emptyList<Community>())
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
        getCommunity("생활")
    }

    private fun getCommunity(type: String) {
        CommunityManager.getCommunity(authToken!!, type) { response ->
            response?.let {
                Log.d("my log", "커뮤니티 목록")
                adapter.updateData(it)
            }
        }
    }

    private fun setupClickListeners() {
        binding.addBtn.setOnClickListener {
            val intent = Intent(activity, AddCommunityActivity::class.java)
            startActivity(intent)
        }

        binding.searchBtn.setOnClickListener {
            val intent = Intent(activity, SearchCommunityActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}