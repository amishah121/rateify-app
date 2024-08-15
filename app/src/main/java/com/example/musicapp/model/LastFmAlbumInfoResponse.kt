package com.example.musicapp.model

data class AlbumInfoResponse(
    val album: AlbumInfoDetails
)

data class AlbumInfoDetails(
    val name: String,
    val artist: String,
    val tracks: TrackList
)

data class TrackList(
    val track: List<Track>
)

data class Track(
    val name: String,
    val duration: String
)
