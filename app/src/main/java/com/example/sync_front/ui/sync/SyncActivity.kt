package com.example.sync_front.ui.sync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.sync_front.databinding.ActivitySyncBinding
import com.example.sync_front.ui.main.home.HomeViewModel
import com.google.android.material.tabs.TabLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sync_front.R


class SyncActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySyncBinding
    private lateinit var circleGraphView: CircleGraphView
    private lateinit var viewModel: SyncViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sync)
        viewModel = ViewModelProvider(this).get(SyncViewModel::class.java)


        binding.viewModel = viewModel  // ViewModel을 바인딩에 연결
        binding.lifecycleOwner = this  // LiveData를 위한 LifecycleOwner 설정

        viewModel.fetchSyncDetail(1, "dfdf")
        setContentView(binding.root)
        setToolbarButton()
        setupTabs(binding.root)
        setupCirCleGraphView()
        observeViewModel()
    }

    private fun observeViewModel() {
        // ViewModel의 LiveData 관찰자 설정
        viewModel.syncDetail.observe(this, Observer {
            // 데이터 변경에 따른 UI 업데이트
        })
    }

    private fun setToolbarButton() {
        binding.syncToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupTabs(view: View) {
        val tabLayout = binding.syncTabs
        tabLayout.addTab(tabLayout.newTab().setText("내외국인"))
        tabLayout.addTab(tabLayout.newTab().setText("성별"))
        tabLayout.addTab(tabLayout.newTab().setText("대학"))
        tabLayout.addTab(tabLayout.newTab().setText("참여수"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab selection
                //updateRecyclerView(tab?.position ?: 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Optional
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Optional
            }
        })
    }

    /*
        private fun updateRecyclerView(index: Int) {
            // Update the data for the RecyclerView based on tab selection
            // This is just an example, replace it with actual data update logic
            if (index == 1) {
                // Change the dataset for 제휴할인
            } else {
                // Change the dataset for 인기싱크
            }
        }*/
    private fun setupCirCleGraphView() {
        circleGraphView = binding.circle
        circleGraphView.animateSections(25f, 25f, 25f, 25f)
    }
}