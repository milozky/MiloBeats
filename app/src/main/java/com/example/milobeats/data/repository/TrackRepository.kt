package com.example.milobeats.data.repository

import com.example.milobeats.data.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun searchTracks(query: String): Flow<List<Track>>
    suspend fun getTrackDetails(trackId: String): Flow<Track>
} 