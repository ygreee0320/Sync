package com.example.sync_front.ui.main.community.image

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.sync_front.databinding.ActivityImageDetailBinding

class ImageDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityImageDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSetting()
    }

    private fun initialSetting() {
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)

        Glide.with(this)
            .load(imageUri)
            .into(binding.image)

        binding.closeBtn.setOnClickListener {
            finish()
        }

        binding.root.setOnClickListener {
            finish()
        }
    }
}