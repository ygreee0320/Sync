package com.example.sync_front.ui.type

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.data.model.Sync
import com.example.sync_front.databinding.ActivityTypeListBinding
import com.example.sync_front.ui.sync.SyncActivity
import com.google.android.material.tabs.TabLayout

class TypeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTypeListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var syncAdapter: SyncSquareAdapter
    private val typeViewModel: TypeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTypeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbarButton()
        setupRecyclerView()
        observeViewModel()
        // 인텐트에서 탭 선택 정보 받기
        val selectedTab = intent.getStringExtra("selectedTab")
        setupTabs(selectedTab)

        typeViewModel.fetchTypeSyncs(syncType = selectedTab) // 해당 타입에 맞게 데이터 요청
    }

    private fun setToolbarButton() {
        binding.listToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupTabs(selectedTab: String?) {
        binding.tabLayout1.apply {
            val tabIndex = when (selectedTab) {
                "일회성" -> 0
                "지속성" -> 1
                else -> 0
            }
            selectTab(getTabAt(tabIndex)) // 탭 활성화
        }
        binding.tabLayout1.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val type = when (tab?.position) {
                    0 -> "일회성"
                    1 -> "지속성"
                    else -> null
                }
                type?.let {
                    // 타입에 따라 ViewModel에서 데이터 불러오기
                    typeViewModel.fetchTypeSyncs(syncType = it)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택 해제되었을 때 필요한 작업
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 탭이 다시 선택되었을 때 필요한 작업
            }
        })
    }

    private fun setupRecyclerView() {
        recyclerView = binding.recyclerView
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
        typeViewModel.typeSyncs.observe(this) { syncs ->
            syncAdapter.updateSyncs(syncs)
        }

        typeViewModel.errorMessage.observe(this, { error ->
            // 에러 메시지 처리, 예를 들어 Toast 메시지를 표시
            //Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })
    }
}
