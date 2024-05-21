package com.example.sync_front.ui.main.home

import SyncPagerAdapter
import android.content.Context
import android.content.Intent
import android.util.Log
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.adapters.ViewBindingAdapter.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.sync_front.R
import com.example.sync_front.data.model.Sync
import com.example.sync_front.databinding.FragmentHomeBinding
import com.example.sync_front.ui.login.LoginActivity
import com.example.sync_front.ui.open.OpenActivity
import com.example.sync_front.ui.sync.SyncActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var syncAdapter: SyncAdapter
    private lateinit var associateAdapter: AssociateSyncAdapter
    private lateinit var name: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel  // ViewModel을 Binding에 연결
        binding.lifecycleOwner = viewLifecycleOwner  // LifecycleOwner 설정

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUser()
        setupRecyclerView()
        subscribeUi()
        setupClickListeners()
    }

    private fun setupUser() {
        // 소셜 로그인으로 얻은 유저이름, 프로필 꺼내기
        val sharedPreferences =
            requireActivity().getSharedPreferences("my_token", Context.MODE_PRIVATE)
        name = sharedPreferences.getString("name", null)!!

        binding.homeUsername.text = name



    }

    private fun setupRecyclerView() {
        syncAdapter = SyncAdapter(listOf(), object : SyncAdapter.OnSyncClickListener {
            override fun onSyncClick(sync: Sync) {
                openSyncActivity(sync)
            }
        })
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = syncAdapter
        }
        associateAdapter = AssociateSyncAdapter(listOf(), object : SyncAdapter.OnSyncClickListener {
            override fun onSyncClick(sync: Sync) {
                openSyncActivity(sync)
            }
        })
        binding.homeDiscountRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = associateAdapter
        }
    }

    private fun openSyncActivity(sync: Sync) {
        val intent = Intent(context, SyncActivity::class.java).apply {
            putExtra("syncId", sync.syncId)
        }
        startActivity(intent)
    }

    private fun subscribeUi() {
        viewModel.fetchSyncs(3) // 데이터 가져오기 호출
        viewModel.fetchAssociateSyncs(3)  // Associate syncs 데이터 불러오기
        viewModel.fetchRecommendSyncs(2)
        // 추천 Syncs 데이터 관찰
        viewModel.recommendSyncs.observe(viewLifecycleOwner) { recommendSyncs ->
            Log.d("HomeFragment", "Recommended Syncs observed: ${recommendSyncs.size}")
            if (recommendSyncs.isNotEmpty()) {
                // ViewPager를 추천 Syncs로 설정
                setupViewPager(recommendSyncs)
            }
        }
        viewModel.syncs.observe(viewLifecycleOwner) { syncs ->
            Log.d("HomeFragment", "Syncs observed: ${syncs.size}")
            if (syncs.isNotEmpty()) {
                syncAdapter.updateSyncs(syncs)
            }
        }
        viewModel.associateSyncs.observe(viewLifecycleOwner) { associateSyncs ->
            if (associateSyncs.isNotEmpty()) {
                associateAdapter.updateSyncs(associateSyncs)
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Log.e("HomeFragment", "Error observed: $message")
        }


    }

    private fun setupViewPager(syncList: List<Sync>) {
        val adapter = SyncPagerAdapter(syncList, object : SyncAdapter.OnSyncClickListener {
            override fun onSyncClick(sync: Sync) {
                openSyncActivity(sync)
            }
        })
        binding.homeVp1.adapter = adapter
        binding.homeVp1.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // ViewPager에 패딩 추가 및 clipToPadding 설정
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)

        // MIN_SCALE과 MIN_ALPHA 값을 여기에 정의
        val MIN_SCALE = 0.85f
        val MIN_ALPHA = 0.5f

        binding.homeVp1.apply {
            setPageTransformer { page, position ->
                val myOffset = position * -(2 * offsetPx + pageMarginPx)
                page.translationX = myOffset
                val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor
                page.alpha =
                    MIN_ALPHA + ((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA))
            }
            setPadding(offsetPx, 0, offsetPx, 0)
        }
    }

    private fun setupClickListeners() {
        binding.fabOpen.setOnClickListener {
            viewModel.authToken?.let { authToken ->
                openOpenActivity(authToken)
            }
        }
    }

    private fun openOpenActivity(token: String) {
        val intent = Intent(context, OpenActivity::class.java).apply {
            putExtra("token", token)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}