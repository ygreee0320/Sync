package com.example.sync_front

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class CircleGraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 80f // 원의 선 두께
    }

    private var sweepAngles = FloatArray(4) // 각 섹션의 각도 저장 배열

    init {
        // 각 섹션에 애니메이션 추가
        for (i in 0..3) {
            animateSection(i, 0f, 0f) // 초기화할 때 모든 섹션을 0%로 설정
        }
    }

    fun animateSection(section: Int, fromProgress: Float, toProgress: Float) {
        if (section in 0..3) {
            val animator = ValueAnimator.ofFloat(fromProgress, toProgress)
            animator.duration = 1000 // 애니메이션 지속 시간을 1초로 설정
            animator.interpolator = LinearInterpolator() // 애니메이션 간에 부드럽게 연결
            animator.addUpdateListener { animation ->
                sweepAngles[section] = 360 * (animation.animatedValue as Float / 100)
                invalidate() // 뷰를 다시 그리도록 요청
            }
            animator.start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val diameter = Math.min(width, height) - paint.strokeWidth
        val rect = RectF(paint.strokeWidth, paint.strokeWidth, diameter, diameter)

        var startAngle = -90f
        val sectionColors =
            arrayOf(R.color.biscay_50, R.color.biscay_30, R.color.biscay_10, R.color.biscay_5)

        for (i in 0 until 4) {
            paint.color = context.getColor(sectionColors[i])
            canvas.drawArc(rect, startAngle, sweepAngles[i], false, paint)
            startAngle += sweepAngles[i]
        }
    }
}
