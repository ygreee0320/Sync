package com.example.sync_front.ui.main.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.databinding.ActivitySelectListBinding

class SelectListActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectListBinding
    private lateinit var adapter: SelectListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()

        // 선택 리스트를 전달받아, 어댑터를 설정하고 리사이클러뷰에 연결
        val itemList = intent.getStringArrayListExtra("itemList")
        if (itemList != null) {
            adapter = SelectListAdapter(itemList)
            binding.recyclerview.layoutManager = LinearLayoutManager(this)
            binding.recyclerview.adapter = adapter
        }
    }

    private fun setupClickListeners() {
        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }
}