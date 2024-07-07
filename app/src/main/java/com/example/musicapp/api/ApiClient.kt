package com.example.musicapp.api

import SpotifyService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.spotify.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val spotifyService: SpotifyService by lazy {
        retrofit.create(SpotifyService::class.java)
    }
}