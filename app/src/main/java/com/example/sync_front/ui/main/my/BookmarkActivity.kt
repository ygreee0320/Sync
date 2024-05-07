package com.example.sync_front.ui.main.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sync_front.databinding.ActivityBookmarkBinding

class BookmarkActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookmarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}