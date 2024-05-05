package com.example.sync_front.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sync_front.ui.main.MainActivity
import com.example.sync_front.BuildConfig.KAKAO_APP_KEY
import com.example.sync_front.api_server.LoginManager
import com.example.sync_front.api_server.Platform
import com.example.sync_front.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 저장된 토큰 꺼내기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("access_token", null)

        KakaoSdk.init(this, "${KAKAO_APP_KEY}")

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                // 소셜 로그인 실패
                Log.d("my log", "로그인 실패")
            } else if (token != null) {
                // 소셜 로그인 성공
                Log.d("my log", "로그인 성공 - 토큰 값 :" + token.accessToken)

                // 토큰 저장
                val sharedPreferences = this.getSharedPreferences("my_token", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                // 서버에 로그인 전송
                LoginManager.sendLogin(
                    token.accessToken, Platform("kakao")
                ) { response ->
                    response?.let {
                        Log.d("my log", "서버 - 로그인 성공")

                        editor.putString("access_token", token.accessToken) // 액세스 토큰 저장
                        editor.putString("email", response.email)
                        editor.putString("name", response.name)
                        editor.apply()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                }

            }
        }

        binding.loginBtn.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }
}