package com.example.musicapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.R
import com.example.musicapp.fragments.AlbumDetailsFragment

class AlbumDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_details)

        // Retrieve intent extras
        val albumName = intent.getStringExtra("albumName")
        val albumCoverUrl = intent.getStringExtra("albumCoverUrl")
        val albumArtist = intent.getStringExtra("albumArtist")

        // Create and set up the Fragment with arguments
        val fragment = AlbumDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("albumName", albumName)
                putString("albumCoverUrl", albumCoverUrl)
                putString("albumArtist", albumArtist)
            }
        }

        // Add or replace the Fragment in the container
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
