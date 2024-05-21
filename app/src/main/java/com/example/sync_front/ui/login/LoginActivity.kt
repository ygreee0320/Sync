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
    private val GOOGLE_LOGIN = 1000 // 구글 로그인 요청 코드
    private var googleApiClient: GoogleApiClient? = null // 구글 로그인 API 클라이언트
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

        // GoogleSignInOptions 설정
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .requestServerAuthCode("${GOOGLE_CLIENT_ID}")
//            .requestIdToken("${GOOGLE_CLIENT_ID}")
//            .build()
//
//        val googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        // GoogleApiClient 초기화
//        googleApiClient = GoogleApiClient.Builder(this)
//            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//            .build()
//        googleApiClient?.connect()
//
//        KakaoSdk.init(this, "${KAKAO_APP_KEY}")

//        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//            if (error != null) { // 카카오 소셜 로그인 실패
//                Log.d("my log", "로그인 실패")
//            } else if (token != null) { // 소셜 로그인 성공
//                Log.d("my log", "로그인 성공 - 토큰 값 :" + token.accessToken)
//
//                // 서버에 로그인 전송
//                sendLoginToServer("kakao", token.accessToken)
//            }
//        }

//        binding.kakaoLoginBtn.setOnClickListener {
//            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
//                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
//            } else {
//                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
//            }
//        }
//
//        binding.googleLoginBtn.setOnClickListener {
//            onLogin(googleSignInClient)
//        }
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

//    override fun onGoogleLogin(client: GoogleSignInClient) {
//        val signInIntent: Intent = client.signInIntent
//        startActivityForResult(signInIntent, GOOGLE_LOGIN)
//    }
//
//    override fun onKakaoLogin(callback: (OAuthToken?, Throwable?) -> Unit) {
//        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
//            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
//        } else {
//            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
//        }
//    }

//    fun onLogin(client: GoogleSignInClient) {
//        val signInIntent: Intent = client.signInIntent
//        startActivityForResult(signInIntent, GOOGLE_LOGIN)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when(requestCode) {
//            GOOGLE_LOGIN -> {
//                try {
//                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//                    Log.d("my log", "$task")
//
//                    val account = task.getResult(ApiException::class.java)
//                    val authCode = task.getResult(ApiException::class.java)?.serverAuthCode
//                    Log.d("my log", "$authCode")
//
//                    Log.d("my log", account.email ?: "")
//                    Log.d("my log", "id 토큰 ${account.idToken}")
//
//                    LoginManager.getAccessToken(authCode!!){ response ->
//                        response?.let {
//                            Log.d("my log", "$response")
//
//                            sendLoginToServer("google", response)
//                        }
//                    }
//
//                } catch (e: ApiException) {
//                    e.printStackTrace()
//                    Log.d("my log", "구글 로그인 실패")
//                }
//            }
//        }
//    }

//    private fun sendLoginToServer(platform: String, accessToken: String) {
//        LoginManager.sendLogin(
//            accessToken, Platform(platform)
//        ) { response ->
//            response?.let {
//                Log.d("my log", "서버 - 로그인 성공")
//
//                // 토큰 저장
//                val sharedPreferences = this.getSharedPreferences("my_token", Context.MODE_PRIVATE)
//                val editor = sharedPreferences.edit()
//
//                editor.putString("access_token", accessToken) // 액세스 토큰 저장
//                editor.putString("auth_token", "Bearer ${response.accessToken}") // api 요청할 authToken
//                editor.putString("email", response.email)
//                editor.putString("name", response.name)
//                editor.putString("sessionId", response.sessionId)
//                editor.apply()
//
//                if (it.isFirst) {
//                    val intent = Intent(this@LoginActivity, OnboardingActivity::class.java)
////                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                    finish()
//                } else {
//                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                    finish()
//                }
//            }
//        }
//    }
}