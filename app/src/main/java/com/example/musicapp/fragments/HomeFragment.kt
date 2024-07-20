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
import com.example.musicapp.model.SpotifyArtist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var newReleasesRecyclerView: RecyclerView
    private lateinit var popAlbumsRecyclerView: RecyclerView
    private lateinit var countryAlbumsRecyclerView: RecyclerView

    private val apiService = ApiClient.spotifyService
    private var accessToken: String? = null

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

        newReleasesRecyclerView = view.findViewById(R.id.newReleasesRecyclerView)
        popAlbumsRecyclerView = view.findViewById(R.id.popAlbumsRecyclerView)
        countryAlbumsRecyclerView = view.findViewById(R.id.countryAlbumsRecyclerView)

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

    private suspend fun fetchNewReleases(accessToken: String): List<SpotifyAlbum> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getNewReleases("Bearer $accessToken")

                if (response.isSuccessful) {
                    val albumsResponse = response.body()
                    val albums = albumsResponse?.albums?.items ?: emptyList()
                    val filteredAlbums = filterTracks(albums)
                    filteredAlbums
                } else {
                    throw Exception("Failed to fetch new releases: ${response.code()}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch new releases: ${e.message}")
            }
        }
    }

    private suspend fun fetchGenreAlbums(accessToken: String, genre: String): List<SpotifyAlbum> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getRecommendations("Bearer $accessToken", genre)

                if (response.isSuccessful) {
                    val recommendationsResponse = response.body()
                    val albums = recommendationsResponse?.tracks?.map { it.album }?.distinct() ?: emptyList()
                    val filteredAlbums = filterTracks(albums)
                    filteredAlbums
                } else {
                    throw Exception("Failed to fetch $genre albums: ${response.code()}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch $genre albums: ${e.message}")
            }
        }
    }

    private suspend fun filterTracks(albums: List<SpotifyAlbum>): List<SpotifyAlbum> {
        val filteredAlbums = albums.mapNotNull { album ->
            val albumResponse = apiService.getAlbumDetails("Bearer $accessToken", album.id)
            if (albumResponse.isSuccessful) {
                val detailedAlbum = albumResponse.body()
                detailedAlbum?.takeIf { it.total_tracks > 1 }
            } else {
                null
            }
        }
        return filteredAlbums
    }


    private fun fetchAlbums() {
        accessToken?.let { token ->
            // Launch coroutine in the main dispatcher
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val newReleases = fetchNewReleases(token)
                    updateRecyclerView(newReleases, newReleasesRecyclerView)

                    val popAlbums = fetchGenreAlbums(token, "pop")
                    updateRecyclerView(popAlbums, popAlbumsRecyclerView)

                    val countryAlbums = fetchGenreAlbums(token, "country")
                    updateRecyclerView(countryAlbums, countryAlbumsRecyclerView)
                    // Fetch and update recommended albums similarly if needed
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("HomeFragment", "Failed to fetch albums: ${e.message}")
                }
            }
        } ?: run {
            Log.e("HomeFragment", "Access token is null, cannot fetch albums.")
        }
    }

    private fun updateRecyclerView(albums: List<SpotifyAlbum>, recyclerView: RecyclerView) {
        // Check if the context and albums are not null
        if (context != null && albums.isNotEmpty()) {
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = AlbumAdapter(albums)
        } else {
            Log.e("HomeFragment", "Context is null or albums list is empty.")
        }
    }
}
