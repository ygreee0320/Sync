package com.example.sync_front.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_onboarding) as NavHostFragment
        val navController = navHostFragment.navController
    }
}