package com.example.musicapp.api

import com.example.musicapp.model.AlbumInfoResponse
import com.example.musicapp.model.AlbumsResponse
import com.example.musicapp.model.SearchAlbumsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmService {

    @GET("/2.0/?method=album.search&format=json")
    suspend fun searchAlbums(
        @Query("album") album: String,
        @Query("api_key") apiKey: String,
    ): Response<SearchAlbumsResponse>

    @GET("/2.0/?method=tag.gettopalbums&format=json")
    suspend fun getTopAlbumsByTag(
        @Query("tag") tag: String,
        @Query("api_key") apiKey: String,
    ): Response<AlbumsResponse>

    @GET("/2.0/?method=album.getinfo&format=json")
    suspend fun getAlbumInfo(
        @Query("artist") artist: String,
        @Query("album") album: String,
        @Query("api_key") apiKey: String
    ): Response<AlbumInfoResponse>
}
