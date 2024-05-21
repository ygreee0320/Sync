package com.example.sync_front.ui.type

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.R
import com.example.sync_front.data.model.Sync
import com.example.sync_front.ui.main.home.SyncAdapter
import com.example.sync_front.ui.sync.SyncActivity

class TypeListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var syncAdapter: SyncSquareAdapter
    private val typeViewModel: TypeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_list)

        setupRecyclerView()
        observeViewModel()
        typeViewModel.fetchTypeSyncs(syncType = "지속성") // 예시로 10개의 데이터를 요청
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView) // XML 파일에서 RecyclerView의 ID를 확인하세요
        syncAdapter = SyncSquareAdapter(listOf(), object : SyncSquareAdapter.OnSyncClickListener {
            override fun onSyncClick(sync: Sync) {
                openSyncActivity(sync)
            }
        })
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TypeListActivity)
            adapter = syncAdapter
        }
    }

    private fun openSyncActivity(sync: Sync) {
        val intent = Intent(this, SyncActivity::class.java).apply {
            putExtra("syncId", sync.syncId)
        }
        startActivity(intent)
    }

    private fun observeViewModel() {
        typeViewModel.typeSyncs.observe(this, { syncs ->
            syncAdapter.updateSyncs(syncs)
        })

        typeViewModel.errorMessage.observe(this, { error ->
            // 에러 메시지 처리, 예를 들어 Toast 메시지를 표시
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })
    }
}
