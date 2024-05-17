package com.example.sync_front.ui.main.my

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.data.model.Sync
import com.example.sync_front.databinding.ActivityBookmarkBinding
import com.example.sync_front.ui.main.home.SyncAdapter
import com.example.sync_front.ui.sync.SyncActivity

class BookmarkActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookmarkBinding
    lateinit var syncList: List<Sync>
    private lateinit var adapter: SyncAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateSyncList()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun updateSyncList() { // 북마크 리스트 출력
        syncList = emptyList<Sync>()
        adapter = SyncAdapter(syncList, object : SyncAdapter.OnSyncClickListener {
            override fun onSyncClick(sync: Sync) {
                openSyncActivity(sync)
            }
        })
        binding.bookmarkRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.bookmarkRecyclerview.adapter = adapter
    }
    private fun openSyncActivity(sync: Sync) {
        val intent = Intent(this, SyncActivity::class.java).apply {
            putExtra("syncId", sync.syncId)
        }
        startActivity(intent)
    }
}