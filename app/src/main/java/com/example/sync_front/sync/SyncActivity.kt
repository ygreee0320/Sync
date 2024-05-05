package com.example.sync_front.sync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sync_front.CircleGraphView
import com.example.sync_front.databinding.ActivitySyncBinding

class SyncActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySyncBinding
    private lateinit var circleGraphView: CircleGraphView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySyncBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarButton()
        setupCirCleGraphView()
    }

    private fun setToolbarButton() {
        binding.syncToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupCirCleGraphView() {
        circleGraphView = binding.circle
        circleGraphView.animateSection(0, 0f, 25f) // 첫 번째 섹션을 0%에서 25%로 애니메이션 적용
        circleGraphView.animateSection(1, 0f, 25f) // 두 번째 섹션을 0%에서 50%로 애니메이션 적용
        circleGraphView.animateSection(2, 0f, 25f) // 세 번째 섹션을 0%에서 75%로 애니메이션 적용
        circleGraphView.animateSection(3, 0f, 25f) // 세 번째 섹션을 0%에서 75%로 애니메이션 적용
    }
}