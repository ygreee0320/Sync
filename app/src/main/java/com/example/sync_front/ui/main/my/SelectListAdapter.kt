package com.example.sync_front.ui.main.my

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.R

class SelectListAdapter(private var list: List<String>): RecyclerView.Adapter<SelectListAdapter.SelectListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_list, parent, false)
        return SelectListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectListViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // 데이터 업데이트 메서드 추가
    fun updateData(newList: List<String>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class SelectListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.text)

        init {
            itemView.setOnClickListener {
                val clickedItem = list[adapterPosition]
                // 클릭한 아이템의 데이터를 Intent에 담아서 이전 액티비티로 전달
                val intent = Intent()
                intent.putExtra("selectedItem", clickedItem)
                Log.d("my log", "선택 결과: $clickedItem")
                (itemView.context as Activity).setResult(Activity.RESULT_OK, intent)
                (itemView.context as Activity).finish() // 현재 액티비티 종료
            }
        }

        fun bind(item: String) {
            textView.text = item
        }

    }
}