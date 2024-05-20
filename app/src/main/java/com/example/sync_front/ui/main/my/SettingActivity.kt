package com.example.sync_front.ui.main.my

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.sync_front.R
import com.example.sync_front.databinding.ActivitySettingBinding
import com.example.sync_front.databinding.PopupLogoutBinding
import com.example.sync_front.ui.login.LoginActivity

class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding
    private var authToken: String ?= null // 로그인 토큰

    companion object {
        private val REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSetting()
        setupClickListeners()
    }

    private fun initialSetting() {
        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)
    }

    private fun setupClickListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.languageLayout.setOnClickListener {
            val itemList = listOf(
                getString(R.string.korean),
                getString(R.string.english),)
            val intent = Intent(this, SelectListActivity::class.java)
            intent.putStringArrayListExtra("itemList", ArrayList(itemList))
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.logout.setOnClickListener {
            showPopup()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedItem = data?.getStringExtra("selectedItem")
            Log.d("my log", "전달 받은 값: $selectedItem")
            binding.language.setText(selectedItem)
        }
    }

    private fun showPopup() {
        val popupLayoutBinding = PopupLogoutBinding.inflate(layoutInflater)
        val popupView = popupLayoutBinding.root

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(popupView)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        // 팝업 레이아웃 내의 버튼에 대한 클릭 리스너
        popupLayoutBinding.doneBtn.setOnClickListener {

            val sharedPreferences = this.getSharedPreferences("my_token", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString("access_token", null) // 리셋
            editor.putString("auth_token", null)
            editor.putString("email", null)
            editor.putString("name", null)
            editor.putString("sessionId", null)
            editor.apply()

            alertDialog.dismiss() // 팝업 닫기

            // Launch LoginActivity and clear the activity stack
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        popupLayoutBinding.cancelBtn.setOnClickListener {
            alertDialog.dismiss() // 팝업 닫기
        }
    }

}