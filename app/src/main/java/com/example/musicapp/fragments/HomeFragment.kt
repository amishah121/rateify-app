package com.example.musicapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.musicapp.R
import com.example.musicapp.adapters.AlbumAdapter
import com.example.musicapp.adapters.SlidesAdapter
import com.example.musicapp.api.ApiClient
import com.example.musicapp.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.musicapp.model.getExtralargeImageUrl

class HomeFragment : Fragment() {

    private lateinit var popAlbumsRecyclerView: RecyclerView
    private lateinit var countryAlbumsRecyclerView: RecyclerView
    private lateinit var hipHopAlbumsRecyclerView: RecyclerView
    private lateinit var rockAlbumsRecyclerView: RecyclerView
    private lateinit var electronicAlbumsRecyclerView: RecyclerView
    private lateinit var rnbAlbumsRecyclerView: RecyclerView
    private lateinit var indieAlbumsRecyclerView: RecyclerView
    private lateinit var jazzAlbumsRecyclerView: RecyclerView
    private lateinit var viewPager: ViewPager

    private val apiService = ApiClient.lastFmService
    private val apiKey = "b6cb1c31d9407ec47411521bfb4d08ed"

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        popAlbumsRecyclerView = view.findViewById(R.id.popAlbumsRecyclerView)
        countryAlbumsRecyclerView = view.findViewById(R.id.countryAlbumsRecyclerView)
        hipHopAlbumsRecyclerView = view.findViewById(R.id.hipHopAlbumsRecyclerView)
        rockAlbumsRecyclerView = view.findViewById(R.id.rockAlbumsRecyclerView)
        electronicAlbumsRecyclerView = view.findViewById(R.id.electronicAlbumsRecyclerView)
        rnbAlbumsRecyclerView = view.findViewById(R.id.rnbAlbumsRecyclerView)
        indieAlbumsRecyclerView = view.findViewById(R.id.indieAlbumsRecyclerView)
        jazzAlbumsRecyclerView = view.findViewById(R.id.jazzAlbumsRecyclerView)
        viewPager = view.findViewById(R.id.imageSlider)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchAlbums()
    }

    private suspend fun fetchTopAlbumsByTag(tag: String): List<Album> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTopAlbumsByTag(tag, apiKey)
                Log.d("Response", response.body().toString())

                if (response.isSuccessful) {
                    val albumsResponse = response.body()
                    val albums = albumsResponse?.albums?.album ?: emptyList()
                    albums
                } else {
                    throw Exception("Failed to fetch albums by tag: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error fetching albums by tag: ${e.message}")
                emptyList()
            }
        }
    }

    private fun fetchAlbums() {
        lifecycleScope.launch {
            try {
                val slidesAlbums = fetchTopAlbumsByTag("disco")
                setupViewPagerWithSlides(slidesAlbums)

                val popAlbums = fetchTopAlbumsByTag("pop")
                updateRecyclerView(popAlbums, popAlbumsRecyclerView)

                val countryAlbums = fetchTopAlbumsByTag("country")
                updateRecyclerView(countryAlbums, countryAlbumsRecyclerView)

                val hipHopAlbums = fetchTopAlbumsByTag("hip-hop")
                updateRecyclerView(hipHopAlbums, hipHopAlbumsRecyclerView)

                val rockAlbums = fetchTopAlbumsByTag("rock")
                updateRecyclerView(rockAlbums, rockAlbumsRecyclerView)

                val electronicAlbums = fetchTopAlbumsByTag("electronic")
                updateRecyclerView(electronicAlbums, electronicAlbumsRecyclerView)

                val rnbAlbums = fetchTopAlbumsByTag("soul")
                updateRecyclerView(rnbAlbums, rnbAlbumsRecyclerView)

                val indieAlbums = fetchTopAlbumsByTag("indie")
                updateRecyclerView(indieAlbums, indieAlbumsRecyclerView)

                val jazzAlbums = fetchTopAlbumsByTag("jazz")
                updateRecyclerView(jazzAlbums, jazzAlbumsRecyclerView)

                // Fetch and update recommended albums similarly if needed

            } catch (e: Exception) {
                Log.e("HomeFragment", "Failed to fetch albums: ${e.message}")
            }
        }
    }

    private fun updateRecyclerView(albums: List<Album>, recyclerView: RecyclerView) {
        if (context != null && albums.isNotEmpty()) {
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            // Set the adapter with the click listener
            recyclerView.adapter = AlbumAdapter(albums) { album ->
                // Create a Bundle to pass album details
                val bundle = Bundle().apply {
                    putString("albumName", album.name)
                    putString("albumCoverUrl", album.getExtralargeImageUrl())
                    putString("albumArtist", album.artist.name)
                    // Add other album details as needed
                }

                // Navigate to AlbumDetailsFragment with the bundle
                val navController = findNavController()
                navController.navigate(R.id.home_to_albumDetails, bundle)
            }
        } else {
            Log.e("HomeFragment", "Context is null or albums list is empty.")
        }
    }



    private fun setupViewPagerWithSlides(albums: List<Album>) {
        val albumImages = albums.mapNotNull { it.getExtralargeImageUrl() }
        val slidesAdapter = SlidesAdapter(albumImages)
        viewPager.adapter = slidesAdapter
        autoScrollHandler.postDelayed(autoScrollRunnable, SCROLL_DELAY)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
    }
}
