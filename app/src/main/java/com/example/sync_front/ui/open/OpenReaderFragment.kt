package com.example.sync_front.ui.open

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import retrofit2.Call
import retrofit2.Response
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.api_server.RetrofitClient
import com.example.sync_front.databinding.FragmentOpenReaderBinding
import com.example.sync_front.ui.main.MainActivity
import com.example.sync_front.data.model.OpenSync
import com.example.sync_front.data.model.PostOpenSync
import com.example.sync_front.data.model.SharedOpenSyncData
import com.example.sync_front.data.service.OpenResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Callback
import java.io.File
import java.io.FileOutputStream
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch

class OpenReaderFragment : Fragment() {
    private var _binding: FragmentOpenReaderBinding? = null
    private val binding get() = _binding!!
    private val openViewModel: OpenViewModel by activityViewModels()
    private var profileUri: Uri? = null  // 프로필 uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOpenReaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        binding.doneBtn.setOnClickListener {
            val currentData = openViewModel.sharedData.value ?: SharedOpenSyncData()
            currentData.userIntro = binding.introduce.text.toString()
            openViewModel.updateData(currentData)
            findNavController().navigate(R.id.action_openLeaderFragment_to_openPreviewFragment)
        }
    }

    private fun observeViewModel() {
        openViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            Log.d("OpenReaderFragment", "Received sync type: ${data.syncType}")
            Log.d("OpenReaderFragment", "Received sync type: ${data.syncName}")
            Log.d("OpenReaderFragment", "Received sync type: ${data.image}")
            Log.d("OpenReaderFragment", "Received sync type: ${data.syncIntro}")
            Log.d("OpenReaderFragment", "Received sync type: ${data.date}")
            Log.d("OpenReaderFragment", "Received sync type: ${data.regularDay}")
            Log.d("OpenReaderFragment", "Received sync type: ${data.regularTime}")
            Log.d("OpenReaderFragment", "Received sync type: ${data.location}")
            Log.d("OpenReaderFragment", "Received sync type: ${data.member_min}")
            Log.d("OpenReaderFragment", "Received sync type: ${data.member_max}")
        }
        profileUri= openViewModel.sharedData.value?.image.toString().toUri()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
