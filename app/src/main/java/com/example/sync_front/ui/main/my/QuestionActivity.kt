package com.example.sync_front.ui.main.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.sync_front.R
import com.example.sync_front.databinding.ActivityQuestionBinding

class QuestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.layout1.setOnClickListener {
            toggleVisibility(binding.ask1, binding.upDown)
        }

        binding.layout2.setOnClickListener {
            toggleVisibility(binding.ask2, binding.upDown2)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun toggleVisibility(view: View, imageView: ImageView) {
        if (view.visibility == View.VISIBLE) {
            view.visibility = View.GONE
            imageView.setImageResource(R.drawable.ic_down)
        } else {
            view.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_up)
        }
    }
}