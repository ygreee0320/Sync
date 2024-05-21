package com.example.sync_front.ui.open

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.R
import com.example.sync_front.ui.open.SelectTheme

data class SelectTheme(
    val icon: String,
    val topic: String,
    val list: List<String>
)

class SelectThemeAdapter(private var list: List<SelectTheme>, private val doneButtonCallback: (Boolean) -> Unit) :
    RecyclerView.Adapter<SelectThemeAdapter.SelectThemeViewHolder>() {

    private val clickedItems: MutableList<String> = mutableListOf() // 클릭된 관심사 리스트

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectThemeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_interest, parent, false)
        return SelectThemeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectThemeViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newList: List<SelectTheme>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class SelectThemeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val topicView: TextView = itemView.findViewById(R.id.topic)
        private val iconView: ImageView = itemView.findViewById(R.id.ic)
        private val upDownView: ImageView = itemView.findViewById(R.id.up_down)
        private val layoutView: LinearLayout = itemView.findViewById(R.id.layout)
        private val layoutDetailView: LinearLayout = itemView.findViewById(R.id.layout_detail)
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerview)

        private var isExpanded = false // 토픽의 토글 상태

        init {
            layoutView.setOnClickListener {
                toggleLayout()
            }
        }

        fun bind(item: SelectTheme) {
            topicView.text = item.topic
            iconView.setImageResource(
                getDrawableResourceId(
                    itemView.context,
                    item.icon
                )
            ) // 아이콘 이미지 설정

            // 토글 상태에 따른 변경
            if (isExpanded) {
                upDownView.setImageResource(R.drawable.ic_up)
                layoutDetailView.visibility = View.VISIBLE
            } else {
                upDownView.setImageResource(R.drawable.ic_down)
                layoutDetailView.visibility = View.GONE
            }

            // 내부 RecyclerView 설정
            recyclerView.apply {
                layoutManager = GridLayoutManager(itemView.context, 3)
                adapter = InnerAdapter(item.list)
            }
        }

        // layoutView 클릭 시 상태를 변경
        private fun toggleLayout() {
            isExpanded = !isExpanded

            // 상태 변경
            if (isExpanded) {
                layoutView.setBackgroundResource(R.color.biscay_5)
                upDownView.setImageResource(R.drawable.ic_up)
                layoutDetailView.visibility = View.VISIBLE
            } else {
                layoutView.setBackgroundResource(android.R.color.white)
                upDownView.setImageResource(R.drawable.ic_down)
                layoutDetailView.visibility = View.GONE
            }
        }

        private fun getDrawableResourceId(context: Context, name: String): Int {
            return context.resources.getIdentifier(name, "drawable", context.packageName)
        }
    }

    inner class InnerAdapter(private val innerList: List<String>) :
        RecyclerView.Adapter<InnerAdapter.InnerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_interest_detail, parent, false)
            return InnerViewHolder(view)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            val item = innerList[position]
            holder.bind(item)
        }

        override fun getItemCount(): Int {
            return innerList.size
        }

        inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val textView: TextView = itemView.findViewById(R.id.content)
            private val select: LinearLayout = itemView.findViewById(R.id.content_layout)
            private var itemName: String = ""

            private var isToggled = false // 아이템의 토글 상태

            init {
                select.setOnClickListener {
                    toggleItem()
                    doneButtonCallback.invoke(clickedItems.size >= 1)

                }
            }

            fun bind(item: String) {
                textView.text = item
                itemName = item

                // 아이템의 토글 상태에 따라 스타일 변경
                if (isToggled) {
                    select.setBackgroundResource(R.drawable.label_default)
                    textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                } else {
                    select.setBackgroundResource(R.drawable.label_white_gray10)
                    textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_70))
                }
            }

            private fun toggleItem() {
                isToggled = !isToggled

                if (isToggled) {
                    select.setBackgroundResource(R.drawable.label_default)
                    textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    if (!clickedItems.contains(itemName)) {
                        clickedItems.add(itemName)
                    }
                } else {
                    select.setBackgroundResource(R.drawable.label_white_gray10)
                    textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_70))
                    if (clickedItems.contains(itemName)) {
                        clickedItems.remove(itemName)
                    }
                }
            }
        }
    }

    // 클릭된 아이템의 텍스트 리스트 반환
    fun getClickedItems(): List<String> {
        return clickedItems.toList()
    }
}