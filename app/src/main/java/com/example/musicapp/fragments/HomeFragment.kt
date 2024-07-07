package com.example.musicapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.activities.SpotifyAuthActivity
import com.example.musicapp.adapters.AlbumAdapter
import com.example.musicapp.api.ApiClient
import com.example.musicapp.model.SpotifyAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var newReleasesRecyclerView: RecyclerView
    private lateinit var popularRightNowRecyclerView: RecyclerView
    private lateinit var recommendedForYouRecyclerView: RecyclerView

    private val apiService = ApiClient.spotifyService
    private var accessToken: String? = null

    // Activity result launcher for starting Spotify authentication activity
    private val startSpotifyAuthActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.hasExtra("ACCESS_TOKEN")) {
                    val token = data.getStringExtra("ACCESS_TOKEN")
                    if (token != null) {
                        accessToken = token
                        Log.d("HomeFragment", "Spotify authentication success. Token: $accessToken")
                        fetchAlbums()
                    } else {
                        Log.e("HomeFragment", "Spotify authentication failed. No token received.")
                    }
                } else {
                    Log.e("HomeFragment", "Spotify authentication failed. Intent data is null or does not contain the access token.")
                }
            } else {
                Log.e("HomeFragment", "Spotify authentication failed. Result code: ${result.resultCode}")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerViews
        newReleasesRecyclerView = view.findViewById(R.id.newReleasesRecyclerView)
        popularRightNowRecyclerView = view.findViewById(R.id.popularRightNowRecyclerView)
        recommendedForYouRecyclerView = view.findViewById(R.id.recommendedForYouRecyclerView)

        // Start Spotify authentication flow if access token is not available
        if (accessToken == null) {
            startSpotifyAuthentication()
        }

        return view
    }

    private fun startSpotifyAuthentication() {
        Log.d("HomeFragment", "Starting Spotify authentication")
        val intent = Intent(requireContext(), SpotifyAuthActivity::class.java)
        startSpotifyAuthActivity.launch(intent)
    }

    private fun fetchAlbums() {
        accessToken?.let { token ->
            Log.d("HomeFragment", "Fetching albums with token: $token")
            // Launch coroutine in the main dispatcher
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    // Fetch new releases
                    val newReleases = fetchNewReleases(token)

                    // Update RecyclerView with fetched albums
                    updateRecyclerView(newReleases, newReleasesRecyclerView)

                } catch (e: Exception) {
                    // Handle errors
                    e.printStackTrace()
                    Log.e("HomeFragment", "Failed to fetch albums: ${e.message}")
                }
            }
        } ?: run {
            Log.e("HomeFragment", "Access token is null, cannot fetch albums.")
        }
    }

    private suspend fun fetchNewReleases(accessToken: String): List<SpotifyAlbum> {
        return withContext(Dispatchers.IO) {
            val response = apiService.getNewReleases("Bearer $accessToken")

            if (response.isSuccessful) {
                val albumsResponse = response.body()
                albumsResponse?.albums?.items ?: emptyList()
            } else {
                throw Exception("Failed to fetch new releases: ${response.code()}")
            }
        }
    }

    private fun updateRecyclerView(albums: List<SpotifyAlbum>, recyclerView: RecyclerView) {
        context?.let {
            recyclerView.layoutManager =
                LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = AlbumAdapter(albums)
        }
    }
}
