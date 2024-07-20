package com.example.musicapp.model

data class SpotifyAlbumsResponse(
    val albums: SpotifyAlbums
)

data class SpotifyRecommendationsResponse(
    val tracks: List<SpotifyTrack>
)

data class SpotifyArtistsResponse(
    val items: List<SpotifyArtist>
)

data class SpotifyAlbums(
    val items: List<SpotifyAlbum>
)

data class SpotifyAlbum(
    val id: String,
    val name: String,
    val artists: List<SpotifyArtist>,
    val images: List<SpotifyImage>,
    val total_tracks: Int
)

data class SpotifyTrack(
    val id: String,
    val name: String,
    val album: SpotifyAlbum
)

data class SpotifyArtist(
    val name: String,
    val id: String
)

data class SpotifyImage(
    val url: String
)
