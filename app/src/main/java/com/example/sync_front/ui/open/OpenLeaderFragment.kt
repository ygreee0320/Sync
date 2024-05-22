package com.example.sync_front.ui.open

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.net.Uri
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.FragmentOpenReaderBinding
import com.example.sync_front.data.model.SharedOpenSyncData

class OpenLeaderFragment : Fragment() {
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
