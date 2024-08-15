package com.example.musicapp.model

import com.google.gson.annotations.SerializedName

data class SearchAlbumsResponse(
    val results: SearchResults
)

data class SearchResults(
    @SerializedName("albummatches")
    val albumMatches: AlbumMatches
)

data class AlbumMatches(
    @SerializedName("album")
    val albumList: List<SearchAlbum>
)

data class SearchAlbum(
    val name: String,
    val artist: String,
    val url: String,
    val image: List<SearchImage>
)

data class SearchImage(
    @SerializedName("#text")
    val url: String,
    val size: String
)

fun SearchAlbum.getExtralargeImageUrl(): String? {
    return image.find { it.size == "extralarge" }?.url
}
