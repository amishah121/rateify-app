package com.example.musicapp.model

import com.google.gson.annotations.SerializedName

data class AlbumsResponse(
    val albums: Albums
)

data class Albums(
    val album: List<Album>
)

data class Album(
    val name: String,
    val artist: Artist,
    val image: List<Image>,
    val url: String
)

data class Artist(
    val name: String
)

data class Image(
    @SerializedName("#text") // Map JSON field name to Kotlin property
    val url: String,
    val size: String
)

fun Album.getExtralargeImageUrl(): String? {
    return image.find { it.size == "extralarge" }?.url
}
