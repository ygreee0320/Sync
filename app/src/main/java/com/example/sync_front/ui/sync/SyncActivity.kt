package com.example.sync_front.ui.sync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.sync_front.databinding.ActivitySyncBinding
import android.util.Log
import com.google.android.material.tabs.TabLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.R
import com.example.sync_front.data.model.Sync
import com.example.sync_front.ui.main.home.SyncAdapter
import java.util.function.LongFunction


class SyncActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySyncBinding
    private lateinit var circleGraphView: CircleGraphView
    private lateinit var viewModel: SyncViewModel
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var sameSyncAdapter: SyncAdapter
    private var syncId: Long = 0
    private var smallerDataName: String = ""
    val token =
        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNzE1NDQ1NTQxLCJleHAiOjE3MTYwNTAzNDF9._EpiWHCK94mi3m9sD4qUX8sYk-Uk2BaSKw8Pbm1U9pM "  // Bearer 키워드 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        syncId = intent.getLongExtra("syncId", -1L)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sync)
        viewModel = ViewModelProvider(this).get(SyncViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.fetchSyncDetail(syncId, token)
        circleGraphView = binding.circle  // circleGraphView 초기화
        setToolbarButton()
        setupTabs(binding.root)
        subscribeUi()
        setupRecyclerView()
        observeViewModel()
    }

    private fun subscribeUi() {
        viewModel.fetchReviews(token, syncId, 3)
        viewModel.fetchSameSyncData(token, syncId, 3)
    }

    private fun observeViewModel() {
        viewModel.reviews.observe(this, Observer {
            reviewAdapter.updateReviews(it)
        })
        viewModel.sames.observe(this, Observer {
            sameSyncAdapter.updateSyncs(it)
        })
        // ViewModel 의 LiveData 관찰자 설정
        viewModel.syncDetail.observe(this, Observer { syncDetail ->
            // regularDate가 null인지 체크
            if (syncDetail.regularDate == null) {
                // regularDate가 null이면, "일시"로 텍스트 설정
                binding.tvDateTitle.text = "일시"
                binding.tvDate.text = syncDetail.date
            } else {
                // regularDate가 null이 아니면, regularDate 값을 텍스트로 설정
                binding.tvDate.text = "${syncDetail.regularDate}\n첫 모임 날짜: ${syncDetail.date}"
            }
            binding.tvCnt.text = "최소 ${syncDetail.userCnt}명 최대 ${syncDetail.totalCnt}명"
        })
        viewModel.graphDetails.observe(this, Observer { details ->
            val graphData = details.data.sortedByDescending { it.percent }
            val sortedData = graphData.map { it.percent.toFloat() }
            Log.d("GraphUpdate", "Updating graph with data: $sortedData")
            when (sortedData.size) {
                0 -> setupCirCleGraphView(0f, 0f)  // 모든 데이터가 없는 경우
                1 -> setupCirCleGraphView(sortedData[0], 0f)
                2 -> {
                    setupCirCleGraphView(sortedData[0], sortedData[1])
                    smallerDataName =
                        if (graphData[0].percent > graphData[1].percent) graphData[1].name else graphData[0].name
                    Log.d("GraphDetail", "Smaller data name: $smallerDataName")
                }

                3 -> setupCirCleGraphView(sortedData[0], sortedData[1], sortedData[2])
                else -> {
                    val topThree = sortedData.take(3)
                    val others = sortedData.drop(3).sum()
                    setupCirCleGraphView(topThree[0], topThree[1], topThree[2], others)
                }
            }
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
                    0 -> {
                        viewModel.fetchGraphData("national", syncId, token)
                        updateGraphTextViews("${smallerDataName}보다 ", "의 비율이 더 높은 편이에요")
                    }

                    1 -> {
                        viewModel.fetchGraphData("gender", syncId, token)
                        updateGraphTextViews("", "의 참여율이 더 높은 편이에요.")
                    }

                    2 -> {
                        viewModel.fetchGraphData("university", syncId, token)
                        updateGraphTextViews("", "의 참여율이 더 높은 편이에요.")
                    }

                    3 -> {
                        viewModel.fetchGraphData("participate", syncId, token)
                        updateGraphTextViews("싱크에 ", " 멤버들이 가장 많은 편이에요.")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        // 첫 번째 탭을 프로그래매틱하게 선택
        tabLayout.post {
            tabLayout.getTabAt(0)?.select()
        }
    }

    private fun updateGraphTextViews(prefix: String, suffix: String) {
        binding.tvGraph1.text = prefix
        viewModel.graphDetails.observe(this, Observer { details ->
            if (details.data.isNotEmpty()) {
                binding.tvGraph2.text = details.status
            }
        })
        binding.tvGraph3.text = suffix
    }


    private fun setupCirCleGraphView(
        first: Float,
        second: Float,
        third: Float? = 0f,
        fourth: Float? = 0f
    ) {
        circleGraphView = binding.circle
        circleGraphView.animateSections(first, second, third, fourth)
    }

    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter(listOf())
        binding.syncReviewRecyclerView.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(context)
        }
        sameSyncAdapter = SyncAdapter(listOf(), object : SyncAdapter.OnSyncClickListener {
            override fun onSyncClick(sync: Sync) {
                openSyncActivity(sync)
            }
        })
        binding.syncSameDayRecyclerView.apply {
            adapter = sameSyncAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun openSyncActivity(sync: Sync) {
        val intent = Intent(this, SyncActivity::class.java).apply {
            putExtra("syncId", sync.syncId)
        }
        startActivity(intent)
    }
}