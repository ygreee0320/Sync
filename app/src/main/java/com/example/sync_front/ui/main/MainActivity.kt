package com.example.sync_front.ui.main

import android.os.Handler
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.ActivityMainBinding
import android.Manifest
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 권한이 허용되었을 때의 처리
            } else {
                // 권한이 거부되었을 때의 처리
            }
        }

    // 뒤로가기 버튼 누른 시간을 기록하기 위한 변수
    private var backPressedTime: Long = 0

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()  // 앱 종료
        } else {
            Toast.makeText(this, "뒤로가기를 한 번 더 누르면 앱이 종료됩니다", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnv.setupWithNavController(navController)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }
    }

    fun showLoadingOverlay(duration: Long) {
        val loadingOverlay = binding.loadingOverlay
        val loadingImageView = binding.loadingImageView

        loadingOverlay.visibility = View.VISIBLE

        Glide.with(this)
            .asGif()
            .load(R.raw.img_trans_loading)
            .into(loadingImageView)

        Handler(Looper.getMainLooper()).postDelayed({
            loadingOverlay.visibility = View.GONE
        }, duration)
    }

    private fun requestNotificationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

}