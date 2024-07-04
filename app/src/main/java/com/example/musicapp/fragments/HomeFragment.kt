package com.example.musicapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.adapters.AlbumAdapter
import com.example.musicapp.model.Album

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val sampleAlbums = listOf(
            Album("1", "Album 1", "Artist 1", "https://example.com/album1.jpg"),
            Album("2", "Album 2", "Artist 2", "https://example.com/album2.jpg"),
            Album("3", "Album 3", "Artist 3", "https://example.com/album3.jpg")
        )

        val newReleasesRecyclerView = view.findViewById<RecyclerView>(R.id.newReleasesRecyclerView)
        newReleasesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newReleasesRecyclerView.adapter = AlbumAdapter(sampleAlbums)

        val popularRightNowRecyclerView = view.findViewById<RecyclerView>(R.id.popularRightNowRecyclerView)
        popularRightNowRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        popularRightNowRecyclerView.adapter = AlbumAdapter(sampleAlbums)

        val recommendedForYouRecyclerView = view.findViewById<RecyclerView>(R.id.recommendedForYouRecyclerView)
        recommendedForYouRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recommendedForYouRecyclerView.adapter = AlbumAdapter(sampleAlbums)

        return view
    }
}