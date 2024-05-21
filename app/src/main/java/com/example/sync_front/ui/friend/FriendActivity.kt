package com.example.sync_front.ui.friend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.databinding.ActivityFriendBinding
import com.example.sync_front.ui.main.home.SyncAdapter
import com.example.sync_front.data.model.Sync
import com.example.sync_front.ui.sync.SyncActivity
import com.example.sync_front.ui.type.SyncSquareAdapter

class FriendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFriendBinding
    private lateinit var viewModel: FriendViewModel
    private lateinit var syncAdapter: SyncSquareAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()
        viewModel.fetchSyncs(null, null)
    }

    private fun setupRecyclerView() {
        syncAdapter = SyncSquareAdapter(listOf(), object : SyncSquareAdapter.OnSyncClickListener {
            override fun onSyncClick(sync: Sync) {
                openSyncActivity(sync)
            }
        })
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = syncAdapter }
    }
    private fun openSyncActivity(sync: Sync) {
        val intent = Intent(this, SyncActivity::class.java).apply {
            putExtra("syncId", sync.syncId)
        }
        startActivity(intent)
    }
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(FriendViewModel::class.java)
        viewModel.friendSyncs.observe(this) { syncs ->
            syncAdapter.updateSyncs(syncs)
        }
        viewModel.errorMessage.observe(this) { message ->
            // Handle errors such as showing a Toast message
        }
    }
}
