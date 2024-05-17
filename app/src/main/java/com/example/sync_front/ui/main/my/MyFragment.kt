package com.example.sync_front.ui.main.my

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.R
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.myProfileBtn.setOnClickListener {
            val intent = Intent(context, ModProfileActivity::class.java)
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
        }

        binding.mySync2.setOnClickListener { // 내가 참여한 싱크 목록
            binding.mySync2Txt.setTextColor(Color.WHITE)
            binding.mySync1Txt.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
            binding.mySync1.setBackgroundResource(R.drawable.bg_btn_mypage_left)
            binding.mySync2.setBackgroundResource(R.drawable.bg_btn_mypage_right_clicked)
        }
    }

    private fun updateSyncList(listType: Int) {
        if (listType == 2) {

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