package com.example.musicapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.adapters.AlbumAdapter
import com.example.musicapp.adapters.SearchAdapter
import com.example.musicapp.api.ApiClient
import com.example.musicapp.model.Album
import com.example.musicapp.model.SearchAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchResultsFragment : Fragment() {

    private lateinit var searchResultsRecyclerView: RecyclerView
    private val apiService = ApiClient.lastFmService
    private val apiKey = "b6cb1c31d9407ec47411521bfb4d08ed"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("SearchResultsFragment", "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val searchEditText = searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(Color.WHITE)
        val iconClose = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        iconClose.setColorFilter(Color.WHITE)
        val iconSearch = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        iconSearch.setColorFilter(Color.WHITE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    // Perform the search
                    performSearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally, you can perform a live search here
                return false
            }
        })

        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView)

        // Retrieve the access token from the fragment arguments
        val query = arguments?.getString("QUERY")

        if (query != null) {
            performSearch(query)
        } else {
            Log.e("SearchResultsFragment", "Query or access token is missing.")
        }
        return view
    }

    private fun performSearch(query: String) {
        Log.d("SearchResultsFragment", "Performing Search")
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.searchAlbums(query, apiKey)
                }

                if (response.isSuccessful) {
                    Log.d("SearchResultsFragment", "Response Body: ${response.body()}")
                    val searchResponse = response.body()
                    val albums = searchResponse?.results?.albumMatches?.albumList ?: emptyList()
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

    private fun updateRecyclerView(albums: List<SearchAlbum>) {
        if (context != null) {
            searchResultsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext())
            searchResultsRecyclerView.adapter = SearchAdapter(albums)
        } else {
            Log.e("SearchResultsFragment", "Context is null, cannot update RecyclerView.")
        }
    }
}
