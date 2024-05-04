package com.example.sync_front

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.util.ResourceBundle

class CircleGraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 110f // 원의 선 두께
        textSize = 56f // 텍스트 크기 설정
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
        val radius = (Math.min(width, height) / 2).toFloat()
        val rect = RectF(
            width / 2 - radius + paint.strokeWidth / 2, height / 2 - radius + paint.strokeWidth / 2,
            width / 2 + radius - paint.strokeWidth / 2, height / 2 + radius - paint.strokeWidth / 2
        )

        var startAngle = -90f
        val sectionColors =
            arrayOf(R.color.biscay_50, R.color.biscay_30, R.color.biscay_10, R.color.biscay_5)

        val textColors =
            arrayOf(R.color.white, R.color.biscay_70, R.color.biscay_50, R.color.biscay_30)

        for (i in 0 until 4) {
            paint.color = context.getColor(sectionColors[i])
            canvas.drawArc(rect, startAngle, sweepAngles[i], false, paint)

            val percentage = (sweepAngles[i] / 360 * 100).toInt()
            if (percentage > 0) { // 0%가 아닐 경우에만 텍스트를 그립니다.
                val sectionCenterAngle = startAngle + sweepAngles[i] / 2
                val sectionCenterX = (width / 2 + (radius - paint.strokeWidth / 2) * Math.cos(
                    Math.toRadians(sectionCenterAngle.toDouble())
                )).toFloat()
                val sectionCenterY = (height / 2 + (radius - paint.strokeWidth / 2) * Math.sin(
                    Math.toRadians(sectionCenterAngle.toDouble())
                )).toFloat()

                val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = context.getColor(textColors[i])
                    textAlign = Paint.Align.CENTER
                    textSize = 40f
                    typeface = ResourcesCompat.getFont(context, R.font.spoqahansansneo_bold)
                }
                val text = "${percentage}%"
                canvas.drawText(
                    text,
                    sectionCenterX,
                    sectionCenterY - (textPaint.descent() + textPaint.ascent()) / 2,
                    textPaint
                )
            }

            startAngle += sweepAngles[i]
        }
    }


}
