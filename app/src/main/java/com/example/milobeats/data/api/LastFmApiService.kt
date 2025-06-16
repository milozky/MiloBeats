package com.example.milobeats.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApiService {

    @GET("?method=track.search&format=json")
    suspend fun searchTracks(
        @Query("track") track: String,
        @Query("api_key") apiKey: String
    ): LastFmSearchResponse

    @GET("?method=track.getInfo&format=json")
    suspend fun getTrackInfo(
        @Query("artist") artist: String,
        @Query("track") track: String,
        @Query("api_key") apiKey: String
    ): LastFmTrackInfoResponse

    @GET("?method=track.getSimilar&format=json")
    suspend fun getSimilarTracks(
        @Query("artist") artist: String,
        @Query("track") track: String,
        @Query("api_key") apiKey: String
    ): LastFmSearchResponse
} 