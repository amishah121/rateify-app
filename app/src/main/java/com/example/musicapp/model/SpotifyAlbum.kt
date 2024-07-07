package com.example.musicapp.model

data class SpotifyAlbum(
    val id: String,
    val name: String,
    val artists: List<SpotifyArtist>,
    val images: List<SpotifyImage>
)
