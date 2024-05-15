package com.example.sync_front.ui.main.community

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.api_server.Comment
import com.example.sync_front.api_server.CommunityManager
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
        authToken = sharedPreferences.getString("access_token", null)

        //임시 토큰 값 (추후 삭제)
        authToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNzE1NDQ1NTQxLCJleHAiOjE3MTYwNTAzNDF9._EpiWHCK94mi3m9sD4qUX8sYk-Uk2BaSKw8Pbm1U9pM "

        commentAdapter = CommentAdapter(emptyList<Comment>())
        binding.commentRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.commentRecyclerview.adapter = commentAdapter

        binding.imgRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = MultiImageLoadAdapter(emptyList(), this)
        binding.imgRecyclerView.adapter = imageAdapter

        loadCommunity()
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun loadCommunity() {
        if (communityId != -1) {
            // 내용 불러오기
            CommunityManager.getCommunityDetail(authToken!!, communityId!!) { response ->
                response?.let {
                    Log.d("my log", "커뮤니티 상세")

                    binding.name.text = it.writerName
                    binding.time.text = it.createdData
                    binding.title.text = it.title
                    binding.content.text = it.content
                    binding.likeCount.text = it.likeCnt.toString()
                    binding.commentCount.text = it.commentCnt.toString()
                    //프로필, 사진들 출력 필요

                    //loadComment() // 댓글 목록 불러오기
                }
            }
        }
    }

    private fun loadComment() {
        if (communityId != -1) {
//            CommunityManager.getCommentList(authToken!!, communityId!!) { response ->
//                response?.let {
//                    Log.d("my log", "댓글 목록")
//
//                    commentAdapter.updateData(it)
//                }
//            }
        }
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