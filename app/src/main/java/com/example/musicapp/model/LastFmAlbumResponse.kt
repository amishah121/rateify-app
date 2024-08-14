package com.example.musicapp.model

import com.google.gson.annotations.SerializedName

data class AlbumsResponse(
    val albums: Albums
)

data class Albums(
    val album: List<Album> // Field name updated to match the API response
)

data class Album(
    val name: String,
    val artist: Artist, // Changed from String to Artist object
    val image: List<Image>,
    val url: String,
    val playcount: String,
    val listeners: String
)

data class Artist(
    val name: String // Adjust as needed based on actual JSON structure
)

data class Image(
    @SerializedName("#text") // Map JSON field name to Kotlin property
    val url: String,
    val size: String
)

fun Album.getExtralargeImageUrl(): String? {
    return image.find { it.size == "extralarge" }?.url
}
