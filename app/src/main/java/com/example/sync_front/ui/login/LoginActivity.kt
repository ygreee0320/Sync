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
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val RC_GOOGLE_SIGN_IN = 1001 // 구글 로그인 요청 코드
    private var googleApiClient: GoogleApiClient? = null // 구글 로그인 API 클라이언트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 저장된 토큰 꺼내기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("access_token", null)

        // GoogleSignInOptions 설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        // GoogleApiClient 초기화
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
        googleApiClient?.connect()

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

        binding.kakaoLoginBtn.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

        binding.googleLoginBtn.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient!!)
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            handleGoogleSignInResult(result)
        }
    }

    private fun handleGoogleSignInResult(result: GoogleSignInResult?) {
        if (result?.isSuccess == true) {
            val account = result.signInAccount
            Log.d("my log", "$account")
            saveAccessToken(account)
            val idToken = account?.idToken
            val accessToken = account?.serverAuthCode

//            Log.d("my log", "$account")
//            Log.d("my log", "$accessToken")
            // 여기서 idToken을 사용하여 서버에 로그인 요청을 보내고 처리할 수 있습니다.
        } else {
            // Google 로그인 실패 처리
        }
    }

    private fun saveAccessToken(account: GoogleSignInAccount?) {
        val accessToken = account?.serverAuthCode // GoogleSignInAccount에서 액세스 토큰 가져오기

        Log.d("my log 액세스", "$account")
//        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("google_access_token", accessToken)
//        editor.apply()
    }
}