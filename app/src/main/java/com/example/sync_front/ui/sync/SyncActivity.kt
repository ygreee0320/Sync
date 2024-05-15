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
    val token =
        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNzE1NDQ1NTQxLCJleHAiOjE3MTYwNTAzNDF9._EpiWHCK94mi3m9sD4qUX8sYk-Uk2BaSKw8Pbm1U9pM "  // Bearer 키워드 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sync)
        viewModel = ViewModelProvider(this).get(SyncViewModel::class.java)


        binding.viewModel = viewModel  // ViewModel을 바인딩에 연결
        binding.lifecycleOwner = this  // LiveData를 위한 LifecycleOwner 설정

        viewModel.fetchSyncDetail(1, token)
        setContentView(binding.root)
        circleGraphView = binding.circle  // circleGraphView 초기화
        setToolbarButton()
        setupTabs(binding.root)
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

        // 탭 선택 리스너 추가
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> setupCirCleGraphView(60f, 40f)
                    1 -> setupCirCleGraphView(48f, 10f, 42f)
                    2 -> setupCirCleGraphView(40f, 10f, 25f, 25f)
                    3 -> setupCirCleGraphView(50f, 20f, 30f)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭 선택이 해제될 때 필요한 작업이 있다면 여기에 추가
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 이미 선택된 탭을 다시 선택했을 때 필요한 작업이 있다면 여기에 추가
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
    private fun setupCirCleGraphView(
        first: Float,
        second: Float,
        third: Float? = 0f,
        fourth: Float? = 0f
    ) {
        circleGraphView = binding.circle
        circleGraphView.animateSections(first, second, third, fourth)
    }
}