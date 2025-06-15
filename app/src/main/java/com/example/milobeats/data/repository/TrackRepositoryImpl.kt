package com.example.milobeats.data.repository

import android.util.Log
import com.example.milobeats.LastFmApiService
import com.example.milobeats.data.model.Track
import com.example.milobeats.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val apiService: LastFmApiService
) : TrackRepository {
    override suspend fun searchTracks(query: String): Flow<List<Track>> = flow {
        try {
            val response = apiService.searchTrack(
                track = query,
                apiKey = "e0eb17c73c6e01c0bfead7461250d9ce"
            )
            Log.d("TrackRepositoryImpl", "API Response: $response")
            emit(response.results.trackMatches?.track ?: emptyList())
        } catch (e: Exception) {
            Log.e("TrackRepositoryImpl", "Error searching tracks", e)
            emit(emptyList())
        }
    }

    override suspend fun getTrackDetails(trackId: String): Flow<Track> = flow {
        // TODO: Implement track details fetching
        throw NotImplementedError("Track details not implemented yet")
    }
} 