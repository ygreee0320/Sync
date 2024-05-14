package com.example.sync_front.ui.main.community

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.api_server.Comment
import com.example.sync_front.databinding.ActivityCommunityDetailBinding

class CommunityDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailBinding
    private lateinit var commentList: List<Comment>
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var imageAdapter: MultiImageLoadAdapter
    private lateinit var imageList: List<String>
    private var authToken: String ?= null // 로그인 토큰
    private var communityId: Int ?= -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSetting()
        setupClickListeners()
    }

    private fun initialSetting() {
        communityId = intent.getIntExtra("communityId", -1)

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        commentAdapter = CommentAdapter(emptyList<Comment>())
        binding.commentRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.commentRecyclerview.adapter = commentAdapter

        binding.imgRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = MultiImageLoadAdapter(emptyList(), this)
        binding.imgRecyclerView.adapter = imageAdapter
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun loadCommunity() {
        if (communityId != -1) {
            // 내용 불러오기

            loadComment() // 댓글 목록 불러오기
        }
    }

    private fun loadComment() {
        // 댓글 목록 불러오기
    }

    private fun extractImage(imageString: String): List<String> {
        // 문자열에서 "[https://"로 시작하고 "," 또는 "]" 전까지의 부분을 추출
        val startIndex = imageString.indexOf("[")
        val endIndex = imageString.indexOf("]", startIndex)

        if (startIndex != -1 && endIndex != -1) {
            val substring = imageString.substring(startIndex, endIndex)
            // "["와  "]"를 제거하고 공백 기준으로 분리하여 리스트로 변환
            return substring.replace("[", "").split(",").map { it.trim() }
        }

        return emptyList()
    }

    override fun onResume() {
        super.onResume()

        loadComment() // 댓글 리스트 업데이트
    }
}