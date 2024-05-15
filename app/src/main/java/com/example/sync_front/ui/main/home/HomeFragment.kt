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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.sync_front.R
import com.example.sync_front.data.model.Sync
import com.example.sync_front.databinding.FragmentHomeBinding
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
        setupTouchListeners()
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
        syncAdapter = SyncAdapter(listOf())
        binding.homeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = syncAdapter
        }
        associateAdapter = AssociateSyncAdapter(listOf())
        binding.homeDiscountRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = associateAdapter
        }
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
        val adapter = SyncPagerAdapter(syncList)
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
                Log.d(
                    "ViewPagerTransform",
                    "Position: $position, Offset: $myOffset, Scale: $scaleFactor"
                )

            }
            // 화면에 보이는 중앙 페이지의 양쪽에 페이지가 보이도록 패딩 설정
            setPadding(offsetPx, 0, offsetPx, 0)
        }
    }

    private fun setupTouchListeners() {
        binding.boxOnetime.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 손가락이 뷰에 닿았을 때 실행되는 애니메이션
                    val scaleDown =
                        AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)
                    view.startAnimation(scaleDown)
                    true // 이벤트 처리가 완료되었음을 시스템에 알림
                }

                MotionEvent.ACTION_UP -> {
                    // 손가락이 뷰에서 떨어졌을 때 실행되는 애니메이션
                    val scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
                    scaleUp.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {}
                        override fun onAnimationEnd(animation: Animation?) {
                            // 애니메이션 종료 후에 다른 작업 수행
                        }

                        override fun onAnimationRepeat(animation: Animation?) {}
                    })
                    view.startAnimation(scaleUp)
                    true // 이벤트 처리가 완료되었음을 시스템에 알림
                }
            }
            false // 여기서 false를 반환하면 onTouchEvent가 이벤트를 계속해서 받지 않음
        }
    }

    private fun setupClickListeners() {
        binding.homeSync3.setOnClickListener {
            // 새로운 Intent 생성. 현재 Fragment의 context와 목표 Activity(SyncActivity::class.java)를 지정
            val intent = Intent(context, SyncActivity::class.java)

            // Intent를 사용하여 Activity 시작
            startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}