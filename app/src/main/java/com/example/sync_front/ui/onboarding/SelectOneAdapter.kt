package com.example.sync_front.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.R

class SelectOneAdapter(private var list: List<String>) :
    RecyclerView.Adapter<SelectOneAdapter.SelectOneViewHolder>() {

    private var selectedItemIndex: Int = RecyclerView.NO_POSITION // 선택된 아이템의 인덱스 (초기엔 없음)
    private var onItemSelectedListener: ((String) -> Unit)? = null  //선택된 아이템 값 넘기기
    private var nextBtnListener: ((Boolean) -> Unit)? = null // 선택된 아이템 리스너 (다음 버튼 활성화)
    private val animatedItems = mutableSetOf<Int>() // 애니메이션이 적용된 아이템 인덱스 저장

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectOneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_select, parent, false)
        return SelectOneViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectOneViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (!animatedItems.contains(position)) { // 애니메이션이 적용되지 않은 경우에만 실행
            val initialTranslationY = 300f
            val duration = 500L
            val interpolator = AccelerateDecelerateInterpolator()

            viewToAnimate.translationY = initialTranslationY
            viewToAnimate.alpha = 0f
            viewToAnimate.animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(interpolator)
                .setDuration(duration)
                .setStartDelay((position * 100).toLong())
                .withEndAction {
                    animatedItems.add(position) // 애니메이션이 끝나면 인덱스를 추가
                }
                .start()
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newList: List<String>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class SelectOneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val selectView: TextView = itemView.findViewById(R.id.select)

        init {
            itemView.setOnClickListener {
                val clickedItem = list[adapterPosition]
                setSelectedItem(adapterPosition) // 선택된 아이템의 인덱스 업데이트

                onItemSelectedListener?.invoke(clickedItem)
            }
        }

        fun bind(item: String) {
            selectView.text = item

            // 아이템 스타일 변경
            if (position == selectedItemIndex) {
                selectView.setTextColor(itemView.context.resources.getColor(R.color.primary))
                selectView.setBackgroundResource(R.drawable.bg_btn_white_pimary)
                selectView.typeface =
                    ResourcesCompat.getFont(itemView.context, R.font.spoqahansansneo_bold)!!
            } else {
                selectView.setTextColor(itemView.context.resources.getColor(R.color.gray_70))
                selectView.setBackgroundResource(R.drawable.bg_btn_box)
                selectView.typeface =
                    ResourcesCompat.getFont(itemView.context, R.font.spoqahansansneo_regular)!!
            }
        }
    }

    fun setSelectedItem(position: Int) {
        selectedItemIndex = position
        notifyDataSetChanged()
        nextBtnListener?.invoke(selectedItemIndex != RecyclerView.NO_POSITION)
    }

    // 아이템 선택 -> 결과값 전송
    fun setOnItemSelectListener(listener: (String) -> Unit) {
        onItemSelectedListener = listener
    }

    // 아이템 선택 -> 다음 버튼 활성화
    fun updateNextBtnListener(listener: (Boolean) -> Unit) {
        nextBtnListener = listener
    }
}