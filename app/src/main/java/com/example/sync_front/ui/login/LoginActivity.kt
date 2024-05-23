package com.example.sync_front.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.sync_front.BuildConfig.GOOGLE_CLIENT_ID
import com.example.sync_front.ui.main.MainActivity
import com.example.sync_front.BuildConfig.KAKAO_APP_KEY
import com.example.sync_front.R
import com.example.sync_front.api_server.LoginManager
import com.example.sync_front.data.model.Platform
import com.example.sync_front.databinding.ActivityLoginBinding
import com.example.sync_front.ui.login.login_viewpager.ViewPager1Fragment
import com.example.sync_front.ui.login.login_viewpager.ViewPager2Fragment
import com.example.sync_front.ui.login.login_viewpager.ViewPager3Fragment
import com.example.sync_front.ui.login.login_viewpager.ViewPagerLoginFragment
import com.example.sync_front.ui.onboarding.OnboardingActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var viewPager: ViewPager2

    class ViewPagerAdapter(
        fragmentManager: FragmentManager, lifecycle: Lifecycle
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return 4
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ViewPager1Fragment()
                1 -> ViewPager2Fragment()
                2 -> ViewPager3Fragment()
                3 -> ViewPagerLoginFragment()
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSetting()
        bindingViewPager()
    }

    private fun initialSetting() {
        // 저장된 토큰 꺼내기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("auth_token", null)

        if (authToken != null) {
            Log.d("my log", "$authToken")

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            this.finish()
        }

    }

    private fun bindingViewPager() {
        viewPager = binding.viewPager
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = pagerAdapter

        // 뷰 페이지 변경 리스너 설정
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 페이지가 변경될 때마다 bottom_bar 이미지 변경
                when (position) {
                    0 -> binding.bottomBar.setImageResource(R.drawable.progressbar_login1)
                    1 -> binding.bottomBar.setImageResource(R.drawable.progressbar_login2)
                    2 -> binding.bottomBar.setImageResource(R.drawable.progressbar_login3)
                    3 -> binding.bottomBar.setImageResource(R.drawable.progressbar_login4)
                }
            }
        })
    }
}