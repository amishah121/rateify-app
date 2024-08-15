package com.example.musicapp.fragments

import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.activities.AlbumDetailsActivity
import com.example.musicapp.adapters.TrackAdapter
import com.example.musicapp.api.ApiClient
import com.example.musicapp.databinding.FragmentAlbumDetailsBinding
import com.example.musicapp.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AlbumDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAlbumDetailsBinding
    private var currentNumber = 0
    private val apiService = ApiClient.lastFmService
    private val apiKey = "b6cb1c31d9407ec47411521bfb4d08ed"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton: View = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            requireActivity().finish()
        }

        // Load album details and tracks
        loadAlbumDetails()

        // Initialize UI components
        setupIncrementDecrement()
    }

    private fun loadAlbumDetails() {
        // Retrieve album details from arguments
        val albumName = arguments?.getString("albumName")
        val albumCoverUrl = arguments?.getString("albumCoverUrl")
        val albumArtist = arguments?.getString("albumArtist")

        // Update UI with the retrieved album details
        binding.albumTitleTextView.text = albumName
        if (albumCoverUrl != null) {
            Glide.with(this)
                .load(albumCoverUrl)
                .into(binding.albumCoverImageView)
        }

        // Fetch album tracks
        if (albumArtist != null) {
            if (albumName != null) {
                fetchAlbumTracks(albumArtist, albumName)
            }
        }
    }

    private fun fetchAlbumTracks(albumArtist: String, albumName: String) {
        // Launch a coroutine to fetch data
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getAlbumInfo(albumArtist, albumName, apiKey)
                }
                if (response.isSuccessful) {
                    val albumInfo = response.body()?.album
                    val tracks = albumInfo?.tracks?.track ?: emptyList()
                    updateTrackList(tracks)
                } else {
                    // Handle API response error
                    Log.e("AlbumDetailsFragment", "Failed to fetch album info: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("AlbumDetailsFragment", "Error fetching album info: ${e.message}")
            }
        }
    }

    private fun updateTrackList(tracks: List<Track>) {
        // Set up RecyclerView with track list
        binding.trackListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.trackListRecyclerView.adapter = TrackAdapter(tracks)
    }

    private fun setupIncrementDecrement() {
        // Set up click listeners for increment and decrement buttons
        binding.incrementButton.setOnClickListener { changeNumber(1) }
        binding.decrementButton.setOnClickListener { changeNumber(-1) }

        // Set initial number display
        binding.numberDisplay.text = currentNumber.toString()
    }

    private fun changeNumber(delta: Int) {
        // Update the current number and ensure it is between 0 and 10
        currentNumber = (currentNumber + delta).coerceIn(0, 10)
        binding.numberDisplay.text = currentNumber.toString()
    }
}
