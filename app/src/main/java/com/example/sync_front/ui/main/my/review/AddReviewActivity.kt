package com.example.sync_front.ui.main.my.review

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import com.example.sync_front.R
import com.example.sync_front.api_server.ReviewManager
import com.example.sync_front.data.model.ReviewModel
import com.example.sync_front.databinding.ActivityAddReviewBinding

class AddReviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddReviewBinding
    private var authToken: String ?= null // 로그인 토큰
    private var syncId: Long ?= -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSetting()
        setupClickListeners()
        setUpChangedListener()
    }

    private fun initialSetting() {
        binding.doneBtn.isEnabled = false

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        // 이름, 싱크네임 바인딩 필요
    }

    private fun setupClickListeners() {
        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                val content = binding.content.text.toString()

                ReviewManager.sendReview(authToken!!, ReviewModel(syncId!!, content)) {
                    if (it?.status == 201) {
                        // 작성 완료 시 이동
                    }
                }
            }
        }
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun setUpChangedListener() { // 이름에 값이 들어갈 때 다음 버튼 활성화
        binding.content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.doneBtn.isEnabled = s?.isNotBlank() ?: false
                updateDoneButtonBackground()
            }
        })
    }

    private fun updateDoneButtonBackground() {
        if (binding.doneBtn.isEnabled) { // 다음 버튼 스타일 변경
            binding.doneBtn.setTextColor(this.resources.getColor(R.color.white))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_default)
        } else {
            binding.doneBtn.setTextColor(this.resources.getColor(R.color.gray_70))
            binding.doneBtn.setBackgroundResource(R.drawable.btn_gray_10)
        }
    }

    private fun hideKeyboard() {
        binding.content.clearFocus()
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}