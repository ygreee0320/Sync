package com.example.sync_front.ui.main.community

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sync_front.R
import com.example.sync_front.api_server.CommunityManager
import com.example.sync_front.data.model.AddCommunity
import com.example.sync_front.databinding.ActivityAddCommunityBinding
import com.example.sync_front.ui.main.my.SelectListActivity
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.ArrayList

class AddCommunityActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddCommunityBinding
    private lateinit var adapter: MultiImageAdapter
    private var uriList = ArrayList<Uri>()  // 선택한 이미지 uri
    private var authToken: String ?= null // 로그인 토큰

    companion object {
        private val maxImage = 5
        private val REQUEST_CODE = 1001
    }

    private val multipleImagePicker =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(maxImage)) { uris: List<Uri>? ->
            if (uris != null) {
                val totalSelectedImages = uriList.size + uris.size
                if (totalSelectedImages > maxImage) {
                    Toast.makeText(applicationContext, "사진은 ${maxImage}장까지 선택 가능합니다.", Toast.LENGTH_LONG)
                        .show()
                } else {
                    // URI에 대한 지속적인 권한을 부여합니다.
                    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    for (uri in uris) {
                        applicationContext.contentResolver.takePersistableUriPermission(uri, flag)
                    }
                    uriList.addAll(0, uris)
                    Log.d("my log", ""+uriList)
                    adapter = MultiImageAdapter(uriList, applicationContext)
                    binding.recyclerView.adapter = adapter
                    binding.picCount.text = uriList.size.toString()
                }
            } else {
                Toast.makeText(applicationContext, "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialSetting()
        setupClickListeners()
        setupTextWatchers()
    }

    private fun initialSetting() {
        binding.doneBtn.isEnabled = false

        // 저장된 토큰 읽어오기
        val sharedPreferences = getSharedPreferences("my_token", MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)

        //임시 토큰 값 (추후 삭제)
        authToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNzE1NDQ1NTQxLCJleHAiOjE3MTYwNTAzNDF9._EpiWHCK94mi3m9sD4qUX8sYk-Uk2BaSKw8Pbm1U9pM"

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = MultiImageAdapter(uriList, this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.categoryBtn.setOnClickListener {
            val itemList = listOf("생활", "질문")
            val intent = Intent(this, SelectListActivity::class.java)
            intent.putStringArrayListExtra("itemList", ArrayList(itemList))
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.addPicBtn.setOnClickListener {
            val remainingImages = maxImage - uriList.size
            if (remainingImages > 0) {
                multipleImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            } else {
                Toast.makeText(applicationContext, "사진은 ${maxImage}장까지 선택 가능합니다.", Toast.LENGTH_LONG).show()
            }
        }

        binding.doneBtn.setOnClickListener {
            if (binding.doneBtn.isEnabled) {
                val type = binding.category.text.toString()
                val title = binding.title.text.toString()
                val content = binding.content.text.toString()
                var imageParts: List<MultipartBody.Part> ?= null

                val community = AddCommunity(type, title, content)
                val gson = Gson()
                val communityJson = gson.toJson(community)
                val requestDtoBody = RequestBody.create("application/json".toMediaTypeOrNull(), communityJson)

                Log.d("my log", "$communityJson $requestDtoBody")

                if (uriList.isNotEmpty()) {
                    imageParts = uriList.map { uri ->
                        val file = File(getRealPathFromURI(uri))
                        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                        MultipartBody.Part.createFormData("images", file.name, requestFile)
                    }
                }
                else {
                    imageParts = null
//                    // 이미지가 없는 경우 빈 이미지를 생성하여 포함
//                    val emptyImageRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), "")
//                    imageParts = listOf(MultipartBody.Part.createFormData("images", "", emptyImageRequestBody))
//                    val emptyImageRequestBody = RequestBody.create("images/\*".toMediaTypeOrNull(), "")
//                    imageParts = listOf(MultipartBody.Part.createFormData("images[]", "", emptyImageRequestBody))
                }

                Log.d("my log", "$imageParts")

                CommunityManager.postCommunity(authToken!!, imageParts, requestDtoBody) { response ->
                    if (response == 200) {
                        Log.d("my log", "게시글 생성 완료!")
                        Toast.makeText(this, "게시글 생성 완료!", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedItem = data?.getStringExtra("selectedItem")
            Log.d("my log", "전달 받은 값: $selectedItem")
            binding.category.setText(selectedItem)
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateDoneButtonState()
                updateEditTextBackgrounds()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.title.addTextChangedListener(textWatcher)
        binding.content.addTextChangedListener(textWatcher)
    }

    private fun updateDoneButtonState() {
        val isTitleFilled = binding.title.text.toString().isNotEmpty()
        val isContentFilled = binding.content.text.toString().isNotEmpty()
        binding.doneBtn.isEnabled = isTitleFilled && isContentFilled
        updateDoneButtonBackground()
    }

    private fun updateEditTextBackgrounds() {
        if (binding.title.text.toString().isNotEmpty()) {
            binding.titleLayout.setBackgroundResource(R.drawable.label_white_primary)
        } else {
            binding.titleLayout.setBackgroundResource(R.drawable.bg_edit_text)
        }

        if (binding.content.text.toString().isNotEmpty()) {
            binding.contentLayout.setBackgroundResource(R.drawable.label_white_primary)
        } else {
            binding.contentLayout.setBackgroundResource(R.drawable.bg_edit_text)
        }
    }

    private fun updateDoneButtonBackground() {
        val btn = binding.doneBtn
        if (btn.isEnabled) { // 다음 버튼 스타일 변경
            btn.setTextColor(this.resources.getColor(R.color.white))
            btn.setBackgroundResource(R.drawable.label_primary_8)
        } else {
            btn.setTextColor(this.resources.getColor(R.color.gray_90))
            btn.setBackgroundResource(R.drawable.label_gray_10_8)
        }
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            it.moveToFirst()
            val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val filePath = it.getString(columnIndex)
            it.close()
            return filePath ?: ""
        }
        return ""
    }

    private fun hideKeyboard() {
        binding.title.clearFocus()
        binding.content.clearFocus()
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}