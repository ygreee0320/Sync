package com.example.sync_front.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sync_front.R
import com.example.sync_front.api_server.EmailManager
import com.example.sync_front.api_server.Onboarding
import com.example.sync_front.data.model.CodeRequest
import com.example.sync_front.data.model.EmailRequest
import com.example.sync_front.databinding.FragmentEmailBinding

class EmailFragment : Fragment() {
    lateinit var binding: FragmentEmailBinding
    private lateinit var language: String
    private var profile: String ?= null
    private lateinit var name: String
    private lateinit var national: String
    private lateinit var gender: String
    private lateinit var univ: String
    private lateinit var email: String
    private val args: EmailFragmentArgs by navArgs()
    private var authToken: String ?= null // 로그인 토큰

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSetting()
        setupClickListeners()
        setUpEmailChangedListener()
        setUpCodeChangedListener()
    }

    private fun initSetting() {
        binding.doneBtn.isEnabled = true
        binding.sendBtn.isEnabled = false
        binding.okayBtn.isEnabled = false

        // 전달된 데이터 읽기
        language = args.onboarding.language!!
        profile = args.onboarding.profile
        name = args.onboarding.userName!!
        national = args.onboarding.countryName!!
        gender = args.onboarding.gender!!
        univ = args.onboarding.university!!

        // 저장된 토큰 읽어오기
        val sharedPreferences = requireActivity().getSharedPreferences("my_token", AppCompatActivity.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        EmailManager.resetCode(authToken!!)
    }

    private fun setupClickListeners() {
        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                val action = EmailFragmentDirections.actionEmailFragmentToTypeFragment(
                    Onboarding(language, profile, name, national, gender, univ, null, null)
                )
                findNavController().navigate(action)
            }
        }

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateUp() // 이전 프래그먼트로
        }

        binding.sendBtn.setOnClickListener {
            if (binding.sendBtn.isEnabled) {
                sendRequest()
            }
        }

        binding.okayBtn.setOnClickListener {
            if (binding.okayBtn.isEnabled) {
                verifyCodeRequest()
            }
        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun sendRequest() { // 인증 코드 전송
        email = binding.email.text.toString()

        EmailManager.sendEmail(authToken!!, EmailRequest(email, univ)) { response ->
            response?.let {
                if (response == 200) {
                    Log.d("my log", "이메일로 인증 코드 요청 완료")
                    Toast.makeText(requireContext(), getString(R.string.send_done), Toast.LENGTH_LONG).show()
                } else {
                    Log.d("my log", "대학과 일치하지 않는 메일 도메인")
                    Toast.makeText(requireContext(), getString(R.string.not_exist_email), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun verifyCodeRequest() { // 인증 코드 확인
        val code = binding.code.text.toString()

        EmailManager.sendCode(authToken!!, CodeRequest(email, univ, code)) { response ->
            response?.let {
                if (response == 200) {
                    Log.d("my log", "인증 성공!")
                    Toast.makeText(requireContext(), getString(R.string.certify_done), Toast.LENGTH_LONG).show()
                    binding.doneBtn.isEnabled = true
                    updateDoneButtonBackground()
                }
                else {
                    Log.d("my log", "인증 실패")
                    Toast.makeText(requireContext(), getString(R.string.re_code), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setUpEmailChangedListener() { // 전송 버튼 활성화
        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.sendBtn.isEnabled = s?.isNotBlank() ?: false
                updateSendButtonBackground()

                if (s.isNullOrEmpty()) {
                    binding.emailLayout.setBackgroundResource(R.drawable.bg_edit_text)
                } else {
                    binding.emailLayout.setBackgroundResource(R.drawable.label_white_primary)
                }
            }
        })
    }

    private fun setUpCodeChangedListener() { // 인증 버튼 활성화
        binding.code.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.okayBtn.isEnabled = s?.isNotBlank() ?: false
                updateOkayButtonBackground()

                if (s.isNullOrEmpty()) {
                    binding.codeLayout.setBackgroundResource(R.drawable.bg_edit_text)
                } else {
                    binding.codeLayout.setBackgroundResource(R.drawable.label_white_primary)
                }
            }
        })
    }

    private fun updateSendButtonBackground() {
        val btn = binding.sendBtn
        if (btn.isEnabled) { // 요청 버튼 스타일 변경
            btn.setTextColor(context!!.resources.getColor(R.color.white))
            btn.setBackgroundResource(R.drawable.btn_default)
        } else {
            btn.setTextColor(context!!.resources.getColor(R.color.gray_70))
            btn.setBackgroundResource(R.drawable.btn_gray_10)
        }
    }

    private fun updateOkayButtonBackground() {
        val btn = binding.okayBtn
        if (btn.isEnabled) { // 확인 버튼 스타일 변경
            btn.setTextColor(context!!.resources.getColor(R.color.white))
            btn.setBackgroundResource(R.drawable.btn_default)
        } else {
            btn.setTextColor(context!!.resources.getColor(R.color.gray_70))
            btn.setBackgroundResource(R.drawable.btn_gray_10)
        }
    }

    private fun updateDoneButtonBackground() {
        val btn = binding.doneBtn
        if (btn.isEnabled) { // 다음 버튼 스타일 변경
            btn.setTextColor(context!!.resources.getColor(R.color.white))
            btn.setBackgroundResource(R.drawable.btn_default)
        } else {
            btn.setTextColor(context!!.resources.getColor(R.color.gray_70))
            btn.setBackgroundResource(R.drawable.btn_gray_10)
        }
    }

    private fun hideKeyboard() {
        binding.email.clearFocus()
        binding.code.clearFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

}