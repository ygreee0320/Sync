package com.example.sync_front.ui.main.my

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.api_server.MypageManager
import com.example.sync_front.data.model.Sync
import com.example.sync_front.databinding.ActivityBookmarkBinding
import com.example.sync_front.ui.main.home.SyncAdapter

class BookmarkActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookmarkBinding
    lateinit var syncList: List<Sync>
    private lateinit var adapter: SyncAdapter
    private var authToken: String ?= null // 로그인 토큰

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSetting()
        updateSyncList()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun initialSetting() {
        // 저장된 토큰 읽어오기
        val sharedPreferences = this.getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        syncList = emptyList<Sync>()
        adapter = SyncAdapter(syncList)
        binding.bookmarkRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.bookmarkRecyclerview.adapter = adapter
    }

    private fun updateSyncList() { // 북마크 리스트 출력
        MypageManager.bookmarkList(authToken!!) { response ->
            if (response?.status == 200 && response.data.isNotEmpty()) {
                response.data.let {
                    Log.d("my log", "북마크 목록")
                    //adapter.updateData(it)

                    binding.empty.visibility = View.GONE
                }
            } else {
                binding.empty.visibility = View.VISIBLE
            }
        }
    }
}