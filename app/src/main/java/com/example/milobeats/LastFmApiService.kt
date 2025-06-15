package com.example.milobeats

import com.example.milobeats.data.model.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApiService {
    @GET("2.0/")
    suspend fun searchTrack(
        @Query("method") method: String = "track.search",
        @Query("track") track: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): TrackSearchResponse
} 