package com.example.musicapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.R
import com.example.musicapp.fragments.AlbumDetailsFragment

class AlbumDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_details)

        val albumName = intent.getStringExtra("albumName")
        val albumCoverUrl = intent.getStringExtra("albumCoverUrl")
        val albumArtist = intent.getStringExtra("albumArtist")

        val fragment = AlbumDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("albumName", albumName)
                putString("albumCoverUrl", albumCoverUrl)
                putString("albumArtist", albumArtist)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}