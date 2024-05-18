package com.example.sync_front.ui.main.my

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sync_front.R
import com.example.sync_front.api_server.MypageManager
import com.example.sync_front.data.model.Sync
import com.example.sync_front.databinding.FragmentMyBinding
import com.example.sync_front.ui.main.home.SyncAdapter
import com.example.sync_front.ui.sync.SyncActivity

class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!
    private var listType: Int? = 1  // 1: 내가 개설한 2: 내가 참여한
    lateinit var syncList: List<Sync>
    private lateinit var adapter: SyncAdapter
    private var authToken: String ?= null // 로그인 토큰

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialSetting()
        setupClickListeners()
    }

    private fun initialSetting() {
        // 저장된 토큰 읽어오기
        val sharedPreferences = requireActivity().getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        adapter = SyncAdapter(emptyList<Sync>(), object : SyncAdapter.OnSyncClickListener {
            override fun onSyncClick(sync: Sync) {
                openSyncActivity(sync)
            }
        })

        binding.syncRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.syncRecyclerview.adapter = adapter

        loadMe() // 내 정보 불러오기
        loadMySyncList()
    }

    private fun loadMe() {
        MypageManager.mypage(authToken!!) { response ->
            if (response?.status == 200) {
                binding.username.text = response.data.name
                binding.userschool.text = response.data.university

                if (!response.data.image.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(response.data.image)
                        .placeholder(R.drawable.img_profile_default)
                        .error(R.drawable.img_profile_default)
                        .into(binding.profileImg)
                } else {
                    binding.profileImg.setImageResource(R.drawable.img_profile_default)
                }
            }
        }
    }

    private fun loadMySyncList() {
        MypageManager.mySyncList(authToken!!) { response ->
            if (response?.status == 200 && response.data.isNotEmpty()) {
                response.data.let {
                    Log.d("my log", "내가 개설한 싱크 목록")
                    adapter.updateSyncs(it)

                    binding.empty1.visibility = View.GONE
                    binding.empty2.visibility = View.GONE
                }
            } else {
                binding.empty1.visibility = View.VISIBLE
            }
        }
    }

    private fun loadMyJoinList() {
        MypageManager.myJoinList(authToken!!) { response ->
            if (response?.status == 200 && response.data.isNotEmpty()) {
                response.data.let {
                    Log.d("my log", "내가 참여한 싱크 목록")
                    adapter.updateSyncs(it)

                    binding.empty1.visibility = View.GONE
                    binding.empty2.visibility = View.GONE
                }
            } else {
                binding.empty2.visibility = View.VISIBLE
            }
        }
    }

    private fun setupClickListeners() {
        binding.myProfileBtn.setOnClickListener {
            val intent = Intent(context, ModProfileActivity::class.java)
            startActivity(intent)
        }

        binding.settingBtn.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }

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

            updateSyncList(1)
        }

        binding.mySync2.setOnClickListener { // 내가 참여한 싱크 목록
            binding.mySync2Txt.setTextColor(Color.WHITE)
            binding.mySync1Txt.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
            binding.mySync1.setBackgroundResource(R.drawable.bg_btn_mypage_left)
            binding.mySync2.setBackgroundResource(R.drawable.bg_btn_mypage_right_clicked)

            updateSyncList(2)
        }
    }

    private fun updateSyncList(listType: Int) {
        if (listType == 2) {
            loadMyJoinList()
        } else {
            // 어댑터를 설정하고 리사이클러뷰에 연결
            syncList = emptyList<Sync>()
            adapter = SyncAdapter(syncList, object : SyncAdapter.OnSyncClickListener {
                override fun onSyncClick(sync: Sync) {
                    openSyncActivity(sync)
                }
            })
            binding.syncRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            binding.syncRecyclerview.adapter = adapter

            loadMySyncList()
        }
    }
    private fun openSyncActivity(sync: Sync) {
        val intent = Intent(context, SyncActivity::class.java).apply {
            putExtra("syncId", sync.syncId)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}