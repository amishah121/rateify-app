package com.example.musicapp.fragments

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.musicapp.R
import com.example.musicapp.activities.SearchResultsActivity
import com.example.musicapp.activities.SpotifyAuthActivity
import com.example.musicapp.adapters.AlbumAdapter
import com.example.musicapp.adapters.SlidesAdapter
import com.example.musicapp.api.ApiClient
import com.example.musicapp.model.SpotifyAlbum
import com.example.musicapp.model.SpotifyImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var newReleasesRecyclerView: RecyclerView
    private lateinit var popAlbumsRecyclerView: RecyclerView
    private lateinit var countryAlbumsRecyclerView: RecyclerView
    private lateinit var hipHopAlbumsRecyclerView: RecyclerView
    private lateinit var rockAlbumsRecyclerView: RecyclerView
    private lateinit var electronicAlbumsRecyclerView: RecyclerView
    private lateinit var rnbAlbumsRecyclerView: RecyclerView
    private lateinit var indieAlbumsRecyclerView: RecyclerView
    private lateinit var jazzAlbumsRecyclerView: RecyclerView
    private lateinit var viewPager: ViewPager

    private val apiService = ApiClient.spotifyService
    private var accessToken: String? = null

    private val autoScrollHandler = Handler(Looper.getMainLooper())
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            val currentItem = viewPager.currentItem
            val nextItem = (currentItem + 1) % (viewPager.adapter?.count ?: 1)
            viewPager.setCurrentItem(nextItem, true)
            autoScrollHandler.postDelayed(this, SCROLL_DELAY)
        }
    }

    private val SCROLL_DELAY = 3000L // 3 seconds


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
        hipHopAlbumsRecyclerView = view.findViewById(R.id.hipHopAlbumsRecyclerView)
        rockAlbumsRecyclerView = view.findViewById(R.id.rockAlbumsRecyclerView)
        electronicAlbumsRecyclerView = view.findViewById(R.id.electronicAlbumsRecyclerView)
        rnbAlbumsRecyclerView = view.findViewById(R.id.rnbAlbumsRecyclerView)
        indieAlbumsRecyclerView = view.findViewById(R.id.indieAlbumsRecyclerView)
        jazzAlbumsRecyclerView = view.findViewById(R.id.jazzAlbumsRecyclerView)
        viewPager = view.findViewById(R.id.imageSlider)

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

    private suspend fun fetchSlides(accessToken: String): List<SpotifyAlbum> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSlides("Bearer $accessToken")

                if (response.isSuccessful) {
                    val recommendationsResponse = response.body()
                    val albums = recommendationsResponse?.tracks?.map { it.album }?.distinct() ?: emptyList()
                    val filteredAlbums = filterTracks(albums)
                    filteredAlbums
                } else {
                    throw Exception("Failed to fetch slides: ${response.code()}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch slides: ${e.message}")
            }
        }
    }

    private fun filterTracks(albums: List<SpotifyAlbum>): List<SpotifyAlbum> {
        val filteredAlbums = albums.mapNotNull { album ->
            album.takeIf { it.total_tracks > 1 }
        }
        return filteredAlbums
    }


    private fun fetchAlbums() {
        accessToken?.let { token ->
            // Launch coroutine in the main dispatcher
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val slidesAlbums = fetchSlides(token)
                    setupViewPagerWithSlides(slidesAlbums)

                    val newReleases = fetchNewReleases(token)
                    updateRecyclerView(newReleases, newReleasesRecyclerView)

                    val popAlbums = fetchGenreAlbums(token, "pop")
                    updateRecyclerView(popAlbums, popAlbumsRecyclerView)

                    val countryAlbums = fetchGenreAlbums(token, "country")
                    updateRecyclerView(countryAlbums, countryAlbumsRecyclerView)

                    val hipHopAlbums = fetchGenreAlbums(token, "hip-hop")
                    updateRecyclerView(hipHopAlbums, hipHopAlbumsRecyclerView)

                    val rockAlbums = fetchGenreAlbums(token, "rock")
                    updateRecyclerView(rockAlbums, rockAlbumsRecyclerView)

                    val electronicAlbums = fetchGenreAlbums(token, "electronic")
                    updateRecyclerView(electronicAlbums, electronicAlbumsRecyclerView)

                    val rnbAlbums = fetchGenreAlbums(token, "soul")
                    updateRecyclerView(rnbAlbums, rnbAlbumsRecyclerView)

                    val indieAlbums = fetchGenreAlbums(token, "indie")
                    updateRecyclerView(indieAlbums, indieAlbumsRecyclerView)

                    val jazzAlbums = fetchGenreAlbums(token, "jazz")
                    updateRecyclerView(jazzAlbums, jazzAlbumsRecyclerView)
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

    private fun setupViewPagerWithSlides(albums: List<SpotifyAlbum>) {
        val albumImages = albums.mapNotNull { it.images.firstOrNull()?.url } // Extract image URLs
        val slidesAdapter = SlidesAdapter(albumImages)
        viewPager.adapter = slidesAdapter
        autoScrollHandler.postDelayed(autoScrollRunnable, SCROLL_DELAY)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
    }
}
