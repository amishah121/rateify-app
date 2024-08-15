package com.example.musicapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentAlbumDetailsBinding

class AlbumDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAlbumDetailsBinding
    private var currentNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the album ID from arguments
        val albumId = arguments?.getString("albumId")

        // Load album details using the albumId (e.g., from a database or API)
        loadAlbumDetails(albumId)

        // Initialize UI components
        setupIncrementDecrement()
    }

    private fun loadAlbumDetails(albumId: String?) {
        // Retrieve album details from arguments
        val albumName = arguments?.getString("albumName")
        val albumCoverUrl = arguments?.getString("albumCoverUrl")
        val albumArtist = arguments?.getString("albumArtist")
        // Update UI with the retrieved album details
        binding.albumTitleTextView.text = albumName
        if (albumCoverUrl != null) {
            // Load image into ImageView, e.g., using Glide or Picasso
            Glide.with(this)
                .load(albumCoverUrl)
                .into(binding.albumCoverImageView)
        }
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
