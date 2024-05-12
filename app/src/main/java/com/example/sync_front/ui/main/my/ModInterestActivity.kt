package com.example.sync_front.ui.main.my

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.databinding.ActivityModInterestBinding

class ModInterestActivity : AppCompatActivity() {
    lateinit var binding: ActivityModInterestBinding
    lateinit var interestList: List<SelectInterest>
    private lateinit var adapter: SelectInterestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModInterestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        updateInterestList()
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        binding.doneBtn.setOnClickListener {
            val clickedItems = adapter.getClickedItems()
            Log.d("my log", "선택된 관심사- $clickedItems")

            val intent = Intent()
            intent.putStringArrayListExtra("selectedItems", ArrayList(clickedItems))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun updateInterestList() { // 관심사 선택 리스트 출력
        val interest1 = SelectInterest("@drawable/ic_exchange_language", "외국어", listOf("언어 교환", "튜터링", "스터디", "기타"))
        val interest2 = SelectInterest("@drawable/ic_culture", "문화·예술", listOf("문학·예술", "영화", "드라마", "미술·디자인", "공연·전시", "음악", "기타"))
        val interest3 = SelectInterest("@drawable/ic_travel", "여행·동행", listOf("관광지", "자연", "휴양", "기타"))
        val interest4 = SelectInterest("@drawable/ic_travel", "액티비티", listOf("러닝·산책", "등산", "클라이밍", "자전거", "축구", "서핑", "테니스", "볼링", "탁구", "기타"))
        val interest5 = SelectInterest("@drawable/ic_travel", "푸드·음료", listOf("맛집", "카페", "술", "기타"))

        interestList = listOf(interest1, interest2, interest3, interest4, interest5)
        adapter = SelectInterestAdapter(interestList)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
    }
}