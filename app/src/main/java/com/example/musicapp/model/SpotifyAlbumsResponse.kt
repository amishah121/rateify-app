package com.example.musicapp.model

data class SpotifyAlbumsResponse(
    val albums: SpotifyAlbums
)

data class SpotifyAlbums(
    val items: List<SpotifyAlbum>
)

data class SpotifyAlbum(
    val id: String,
    val name: String,
    val artists: List<SpotifyArtist>,
    val images: List<SpotifyImage>
)

data class SpotifyArtist(
    val name: String
)

data class SpotifyImage(
    val url: String
)
