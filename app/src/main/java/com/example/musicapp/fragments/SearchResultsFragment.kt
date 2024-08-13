package com.example.musicapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.adapters.AlbumAdapter
import com.example.musicapp.api.ApiClient
import com.example.musicapp.model.SpotifyAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchResultsFragment : Fragment() {

    private lateinit var searchResultsRecyclerView: RecyclerView
    private val apiService = ApiClient.spotifyService
    private var accessToken: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("SearchResultsFragment", "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)

        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView)

        // Retrieve the access token from the fragment arguments
        accessToken = arguments?.getString("ACCESS_TOKEN")
        val query = arguments?.getString("QUERY")

        if (query != null && accessToken != null) {
            performSearch(query, accessToken!!)
        } else {
            Log.e("SearchResultsFragment", "Query or access token is missing.")
        }

        return view
    }

    private fun performSearch(query: String, accessToken: String) {
        Log.d("SearchResultsFragment", "Performing Search")
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.search("Bearer $accessToken", query, "album")
                }

                if (response.isSuccessful) {
                    val albumsResponse = response.body()
                    val albums = albumsResponse?.albums?.items ?: emptyList()
                    updateRecyclerView(albums)
                    Log.d("SearchResultsFragment", "Albums: $albums")
                } else {
                    Log.e("SearchResultsFragment", "Failed to fetch search results: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("SearchResultsFragment", "Error performing search: ${e.message}")
            }
        }
    }

    private fun updateRecyclerView(albums: List<SpotifyAlbum>) {
        if (context != null) {
            searchResultsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext())
            searchResultsRecyclerView.adapter = AlbumAdapter(albums)
        } else {
            Log.e("SearchResultsFragment", "Context is null, cannot update RecyclerView.")
        }
    }
}
