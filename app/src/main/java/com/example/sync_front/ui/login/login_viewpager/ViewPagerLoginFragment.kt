package com.example.sync_front.ui.login.login_viewpager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sync_front.BuildConfig
import com.example.sync_front.BuildConfig.GOOGLE_CLIENT_ID
import com.example.sync_front.api_server.LoginManager
import com.example.sync_front.data.model.Platform
import com.example.sync_front.databinding.FragmentViewPagerLoginBinding
import com.example.sync_front.ui.main.MainActivity
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

class ViewPagerLoginFragment : Fragment() {
    private lateinit var binding: FragmentViewPagerLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val GOOGLE_LOGIN = 1000 // 구글 로그인 요청 코드
    private var googleApiClient: GoogleApiClient? = null // 구글 로그인 API 클라이언트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPagerLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // GoogleSignInOptions 초기화
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestServerAuthCode(GOOGLE_CLIENT_ID)
            .requestIdToken(GOOGLE_CLIENT_ID)
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // GoogleApiClient 초기화
        googleApiClient = GoogleApiClient.Builder(requireActivity())
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
        googleApiClient?.connect()

        KakaoSdk.init(requireActivity(), "${BuildConfig.KAKAO_APP_KEY}")

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) { // 카카오 소셜 로그인 실패
                Log.d("my log", "로그인 실패")
            } else if (token != null) { // 소셜 로그인 성공
                Log.d("my log", "로그인 성공 - 토큰 값 :" + token.accessToken)

                // 서버에 로그인 전송
                sendLoginToServer("kakao", token.accessToken)
            }
        }

        binding.kakaoLoginBtn.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireActivity())) {
                UserApiClient.instance.loginWithKakaoTalk(requireActivity(), callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireActivity(), callback = callback)
            }
        }

        binding.googleLoginBtn.setOnClickListener {
            onLogin(googleSignInClient)
        }
    }

    fun onLogin(client: GoogleSignInClient) {
        val signInIntent: Intent = client.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            GOOGLE_LOGIN -> {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    Log.d("my log", "$task")

                    val account = task.getResult(ApiException::class.java)
                    val authCode = task.getResult(ApiException::class.java)?.serverAuthCode
                    Log.d("my log", "$authCode")

                    Log.d("my log", account.email ?: "")
                    Log.d("my log", "id 토큰 ${account.idToken}")

                    LoginManager.getAccessToken(authCode!!){ response ->
                        response?.let {
                            Log.d("my log", "$response")

                            sendLoginToServer("google", response)
                        }
                    }

                } catch (e: ApiException) {
                    e.printStackTrace()
                    Log.d("my log", "구글 로그인 실패")
                }
            }
        }
    }

    private fun sendLoginToServer(platform: String, accessToken: String) {
        LoginManager.sendLogin(
            accessToken, Platform(platform)
        ) { response ->
            response?.let {
                Log.d("my log", "서버 - 로그인 성공")

                // 토큰 저장
                val sharedPreferences = requireContext().getSharedPreferences("my_token", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                editor.putString("access_token", accessToken) // 액세스 토큰 저장
                editor.putString("auth_token", "Bearer ${response.accessToken}") // api 요청할 authToken
                editor.putString("email", response.email)
                editor.putString("name", response.name)
                editor.putString("sessionId", response.sessionId)
                editor.apply()

                if (it.isFirst) {
                    val intent = Intent(requireContext(), OnboardingActivity::class.java)
//                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    //finish()
                } else {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    //finish()
                }
            }
        }
    }

}