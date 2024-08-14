package com.example.musicapp.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://ws.audioscrobbler.com/2.0/"

    private val gson: Gson = GsonBuilder()
        .setLenient() // Allows lenient JSON parsing
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val lastFmService: LastFmService by lazy {
        retrofit.create(LastFmService::class.java)
    }
}
