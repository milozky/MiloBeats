package com.example.milobeats

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LastFmApiClient {
    private const val BASE_URL = "https://ws.audioscrobbler.com/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val apiService: LastFmApiService by lazy {
        retrofit.create(LastFmApiService::class.java)
    }

    fun searchTrack(trackName: String, apiKey: String) {
        val call = apiService.searchTrack(track = trackName, apiKey = apiKey)
        call.enqueue(object : retrofit2.Callback<TrackSearchResponse> {
            override fun onResponse(
                call: retrofit2.Call<TrackSearchResponse>,
                response: retrofit2.Response<TrackSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val trackSearchResponse = response.body()
                    // Handle the response
                    println("Tracks found: ${trackSearchResponse?.results?.trackMatches?.track}")
                } else {
                    // Handle error
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<TrackSearchResponse>, t: Throwable) {
                // Handle failure
                println("Failure: ${t.message}")
            }
        })
    }
} 