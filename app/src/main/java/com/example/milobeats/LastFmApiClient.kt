package com.example.milobeats

import com.example.milobeats.data.model.TrackSearchResponse
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LastFmApiClient {
    private const val BASE_URL = "https://ws.audioscrobbler.com/"

    private val gson = GsonBuilder()
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: LastFmApiService by lazy {
        retrofit.create(LastFmApiService::class.java)
    }
} 