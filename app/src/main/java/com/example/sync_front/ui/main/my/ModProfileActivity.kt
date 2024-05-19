package com.example.sync_front.ui.main.my

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sync_front.R
import com.example.sync_front.databinding.ActivityModProfileBinding

class ModProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityModProfileBinding
    val REQUEST_CODE = 1001
    val REQUEST_CODE2 = 1002
    val REQUEST_CODE3 = 1003
    private var profile: Uri ?= null  // 이미지 추가

    private val singleImagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                profile = uri

                binding.profileImg.setImageURI(uri)

                // URI에 대한 지속적인 권한을 부여
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                applicationContext.contentResolver.takePersistableUriPermission(uri, flag)
            } else {
                Toast.makeText(applicationContext, getString(R.string.didnt_select_img), Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.profileImg.setOnClickListener {
            singleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }

        binding.genderLayout.setOnClickListener {
            val itemList = listOf(getString(R.string.woman), getString(R.string.man), getString(R.string.closed))
            val intent = Intent(this, SelectListActivity::class.java)
            intent.putStringArrayListExtra("itemList", ArrayList(itemList))
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.likePeopleLayout.setOnClickListener {
            val itemList = listOf(
                getString(R.string.type1),
                getString(R.string.type2),
                getString(R.string.type3))
            val intent = Intent(this, SelectListActivity::class.java)
            intent.putStringArrayListExtra("itemList", ArrayList(itemList))
            startActivityForResult(intent, REQUEST_CODE2)
        }

        binding.likeLayout.setOnClickListener {
            val intent = Intent(this, ModInterestActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE3)
        }

        binding.doneBtn.setOnClickListener { // 수정 완료

        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        binding.root.setOnClickListener {
            // 화면의 다른 부분을 클릭하면 EditText의 포커스를 해제하고 키보드를 내림
            hideKeyboard()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedItem = data?.getStringExtra("selectedItem")
            Log.d("my log", "전달 받은 값: $selectedItem")
            binding.gender.setText(selectedItem)
        }
        else if (requestCode == REQUEST_CODE2 && resultCode == Activity.RESULT_OK) {
            val selectedItem = data?.getStringExtra("selectedItem")
            Log.d("my log", "전달 받은 값: $selectedItem")
            binding.likePeople.setText(selectedItem)
        }
        else if (requestCode == REQUEST_CODE3 && resultCode == Activity.RESULT_OK) {
            val selectedItems = data?.getStringArrayListExtra("selectedItems")
            Log.d("my log", "전달 받은 값: $selectedItems")

            val selectedItemsText = selectedItems?.joinToString(", ")
            binding.like.setText(selectedItemsText)
        }
    }

    private fun hideKeyboard() {
        binding.username.clearFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}