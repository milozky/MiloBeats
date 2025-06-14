package com.example.milobeats.data.repository

import com.example.milobeats.data.api.LastFmApiService
import com.example.milobeats.data.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val apiService: LastFmApiService
) : TrackRepository {
    
    override suspend fun searchTracks(query: String): Flow<List<Track>> = flow {
        try {
            val response = apiService.searchTrack(track = query, apiKey = "e0eb17c73c6e01c0bfead7461250d9ce")
            response.execute().body()?.results?.trackmatches?.track?.let { tracks ->
                emit(tracks)
            } ?: emit(emptyList())
        } catch (e: Exception) {
            // In a real app, we would handle errors properly
            emit(emptyList())
        }
    }

    override suspend fun getTrackDetails(trackId: String): Flow<Track> = flow {
        // Implementation for getting track details
        // This would be implemented when we add the track details endpoint
        throw NotImplementedError("Track details not implemented yet")
    }
} 