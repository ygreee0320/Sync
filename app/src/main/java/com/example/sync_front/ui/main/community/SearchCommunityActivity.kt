package com.example.sync_front.ui.main.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sync_front.R
import com.example.sync_front.databinding.ActivitySearchCommunityBinding

class SearchCommunityActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSetting()
        setupClickListeners()
    }

    private fun initialSetting() {

    }

    private fun setupClickListeners() {
        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }
}