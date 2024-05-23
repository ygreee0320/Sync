package com.example.sync_front.ui.associate

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sync_front.api_server.RetrofitClient
import com.example.sync_front.data.model.Sync
import com.example.sync_front.data.service.AssociateSyncRequest
import com.example.sync_front.data.service.SyncResponse
import com.example.sync_front.databinding.ActivityAssociateBinding
import com.example.sync_front.ui.main.home.AssociateSyncAdapter
import com.example.sync_front.ui.main.home.SyncAdapter
import com.example.sync_front.ui.sync.SyncActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssociateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssociateBinding
    private var authToken: String? = null // 로그인 토큰
    private lateinit var associateAdapter: AssociateSyncAdapter
    val associateSyncs = MutableLiveData<List<Sync>>()
    val errorMessage = MutableLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssociateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarButton()
        initialSetting()
        fetchAssociateSyncs()
        setupRecyclerView()
        setupObservers()

    }

    private fun initialSetting() {
        // 저장된 토큰 읽어오기
        val sharedPreferences = application.getSharedPreferences("my_token", Context.MODE_PRIVATE)
        authToken = sharedPreferences.getString("auth_token", null)
    }

    private fun setToolbarButton() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        associateAdapter = AssociateSyncAdapter(listOf(), object : SyncAdapter.OnSyncClickListener {
            override fun onSyncClick(sync: Sync) {
                openSyncActivity(sync)
            }
        })
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@AssociateActivity, 2)
            adapter = associateAdapter
        }
    }

    private fun openSyncActivity(sync: Sync) {
        val intent = Intent(this, SyncActivity::class.java).apply {
            putExtra("syncId", sync.syncId)
        }
        startActivity(intent)
    }

    private fun setupObservers() {
        associateSyncs.observe(this, { syncs ->
            associateAdapter.updateSyncs(syncs)  // 어댑터에 데이터 업데이트
        })

        errorMessage.observe(this, { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })
    }


    fun fetchAssociateSyncs(take: Int? = null, syncType: String? = null, type: String? = null, language: String? = null) {
        val request = AssociateSyncRequest(take, syncType, type, language)
        RetrofitClient.instance.homeService.postAssociateSyncs(
            "application/json",
            authToken,
            request
        )
            .enqueue(object : Callback<SyncResponse> {
                override fun onResponse(
                    call: Call<SyncResponse>,
                    response: Response<SyncResponse>
                ) {
                    if (response.isSuccessful) {
                        associateSyncs.postValue(response.body()?.data ?: listOf())
                    } else {
                        if (response.code() == 401) { // 토큰 만료 체크
                            errorMessage.postValue("Token expired. Please log in again.")
                        } else {
                            errorMessage.postValue("Error: ${response.errorBody()?.string()}")
                        }
                    }
                }

                override fun onFailure(call: Call<SyncResponse>, t: Throwable) {
                    errorMessage.postValue(t.message ?: "Unknown error")
                }
            })
    }
}