package com.example.sync_front.ui.main.my

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.R
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
        val interest1 = SelectInterest("@drawable/ic_exchange_language", getString(R.string.foreignLanguage),
            listOf(getString(R.string.languageExchange), getString(R.string.tutoring), getString(R.string.study)))
        val interest2 = SelectInterest("@drawable/ic_culture", getString(R.string.cultureArt),
            listOf(getString(R.string.movie), getString(R.string.drama), getString(R.string.art), getString(R.string.performance), getString(R.string.music)))
        val interest3 = SelectInterest("@drawable/ic_travel", getString(R.string.travelCompanion),
            listOf(getString(R.string.sightseeing), getString(R.string.nature), getString(R.string.vacation)))
        val interest4 = SelectInterest("@drawable/ic_activity", getString(R.string.activity),
            listOf(getString(R.string.running), getString(R.string.hiking), getString(R.string.climbing),
                getString(R.string.bike), getString(R.string.soccer), getString(R.string.surfing),
                getString(R.string.tennis), getString(R.string.bowling), getString(R.string.tableTennis)))
        val interest5 = SelectInterest("@drawable/ic_food", getString(R.string.foodAndDrink),
            listOf(getString(R.string.restaurant), getString(R.string.cafe), getString(R.string.drink)))
        val interest6 = SelectInterest("@drawable/ic_etc", getString(R.string.etc),
            listOf(getString(R.string.etc)))

        interestList = listOf(interest1, interest2, interest3, interest4, interest5, interest6)
        adapter = SelectInterestAdapter(interestList) {
            // 완료 버튼 활성화 필요
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
    }
}