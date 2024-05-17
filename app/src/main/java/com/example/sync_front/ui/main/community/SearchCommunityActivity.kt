package com.example.sync_front.ui.main.community

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.R
import com.example.sync_front.api_server.CommunityManager
import com.example.sync_front.data.model.Community
import com.example.sync_front.databinding.ActivitySearchCommunityBinding

class SearchCommunityActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchCommunityBinding
    private lateinit var adapter: CommunityAdapter
    private var authToken: String ?= null // 로그인 토큰

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSetting()
        setupClickListeners()
        setupActionListener()
    }

    private fun initialSetting() {
        // 저장된 토큰 읽어오기
        val sharedPreferences = this.getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)
        Log.d("my log", "현재 토큰 값: $authToken")

        adapter = CommunityAdapter(emptyList<Community>())
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
    }

    private fun setupActionListener() { // 검색
        binding.searchText.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = textView.text.toString()
                if (keyword.isNotEmpty()) {
                    searchCommunity(keyword)
                    hideKeyboard()
                } else {
                    Toast.makeText(this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }

        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.textCancel.visibility = View.GONE
                    binding.searchLayout.setBackgroundResource(R.drawable.bg_edit_text)
                } else {
                    binding.textCancel.visibility = View.VISIBLE
                    binding.searchLayout.setBackgroundResource(R.drawable.label_white_primary)
                }
            }
        })
    }

    private fun setupClickListeners() {
        binding.textCancel.setOnClickListener {
            binding.searchText.text.clear()
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun searchCommunity(keyword: String) {
        CommunityManager.searchCommunity(authToken!!, keyword) { response ->
            if (response!!.status == 200 && response.data.isNotEmpty()) {
                response.data.let {
                    Log.d("my log", "커뮤니티 목록")
                    //adapter.updateData(it)
                }
                binding.initLayout.visibility = View.GONE
            } else {
                binding.initLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun hideKeyboard() {
        binding.searchText.clearFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}