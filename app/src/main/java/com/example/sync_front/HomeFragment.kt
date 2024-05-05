package com.example.sync_front

import com.example.sync_front.CircleGraphView
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.viewpager2.widget.ViewPager2
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.databinding.FragmentHomeBinding
import com.example.sync_front.sync.SyncActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var circleGraphView: CircleGraphView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTouchListeners()
        setupClickListeners()
    }


    private fun setupViewPager() {
        binding.viewPagerSync.adapter = ViewPagerAdapter(getSyncList())
        binding.viewPagerSync.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPagerSync.offscreenPageLimit = 3

        // ViewPager에 패딩 추가 및 clipToPadding 설정
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)

        // MIN_SCALE과 MIN_ALPHA 값을 여기에 정의
        val MIN_SCALE = 0.85f
        val MIN_ALPHA = 0.5f

        binding.viewPagerSync.apply {
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

    private fun getSyncList(): ArrayList<Int> {
        return arrayListOf<Int>(
            R.drawable.container_sync_box,
            R.drawable.container_sync_box,
            R.drawable.container_sync_box
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}