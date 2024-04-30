package com.example.sync_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sync_front.databinding.ActivityChattingBinding

class ChattingActivity : AppCompatActivity() {
    lateinit var binding: ActivityChattingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}