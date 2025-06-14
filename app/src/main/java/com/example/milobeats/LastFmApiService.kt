package com.example.milobeats

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApiService {
    @GET("2.0/")
    fun searchTrack(
        @Query("method") method: String = "track.search",
        @Query("track") track: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): Call<TrackSearchResponse>
}

data class TrackSearchResponse(
    val results: Results
)

data class Results(
    val trackMatches: TrackMatches
)

data class TrackMatches(
    val track: List<Track>
)

data class Track(
    val name: String,
    val artist: String,
    val url: String
) 