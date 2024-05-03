package com.example.sync_front

import com.example.sync_front.CircleGraphView
import android.animation.ValueAnimator
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
import com.example.sync_front.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var circleGraphView: CircleGraphView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupCirCleGraphView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTouchListeners()
    }

    private fun setupCirCleGraphView() {
        circleGraphView = binding.circle
        circleGraphView.animateSection(0, 0f, 25f) // 첫 번째 섹션을 0%에서 25%로 애니메이션 적용
        circleGraphView.animateSection(1, 0f, 25f) // 두 번째 섹션을 0%에서 50%로 애니메이션 적용
        circleGraphView.animateSection(2, 0f, 25f) // 세 번째 섹션을 0%에서 75%로 애니메이션 적용

    }

    private fun setupViewPager() {
        binding.viewPagerSync.adapter = ViewPagerAdapter(getSyncList())
        binding.viewPagerSync.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPagerSync.setPageTransformer(ZoomOutPageTransformer())
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


    private fun getSyncList(): ArrayList<Int> {
        return arrayListOf<Int>(
            R.drawable.container_sync_box,
            R.drawable.container_sync_box,
            R.drawable.container_sync_box
        )
    }

    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        private val MIN_SCALE = 0.85f
        private val MIN_ALPHA = 0.5f
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }

                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }

                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}