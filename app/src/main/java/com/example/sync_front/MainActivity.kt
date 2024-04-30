package com.example.sync_front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.sync_front.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()

        // 바텀 네비게이션 - 모임 메뉴 클릭 시 HomeFragment 로드
        binding.menu1.setOnClickListener {
            loadFragment(HomeFragment())
            binding.menu1.setImageResource(R.drawable.menu_gathering_clicked)
            binding.menu2.setImageResource(R.drawable.menu_mentoring)
            binding.menu3.setImageResource(R.drawable.menu_chatting)
            binding.menu4.setImageResource(R.drawable.menu_profile)
        }

        binding.menu2.setOnClickListener {
            //loadFragment(HomeFragment())
            binding.menu1.setImageResource(R.drawable.menu_gathering)
            binding.menu2.setImageResource(R.drawable.menu_mentoring_clicked)
            binding.menu3.setImageResource(R.drawable.menu_chatting)
            binding.menu4.setImageResource(R.drawable.menu_profile)
        }

        binding.menu3.setOnClickListener {
            loadFragment(ChattingFragment())
            binding.menu1.setImageResource(R.drawable.menu_gathering)
            binding.menu2.setImageResource(R.drawable.menu_mentoring)
            binding.menu3.setImageResource(R.drawable.menu_chatting_clicked)
            binding.menu4.setImageResource(R.drawable.menu_profile)
        }

        binding.menu4.setOnClickListener {
            //loadFragment(HomeFragment())
            binding.menu1.setImageResource(R.drawable.menu_gathering)
            binding.menu2.setImageResource(R.drawable.menu_mentoring)
            binding.menu3.setImageResource(R.drawable.menu_chatting)
            binding.menu4.setImageResource(R.drawable.menu_profile_clicked)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // 백 스택에 추가하여 뒤로가기 버튼으로 이전 상태로 돌아갈 수 있도록
            .commit()
    }
}