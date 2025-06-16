package com.example.milobeats.domain.repository

import com.example.milobeats.domain.model.MusicSource
import com.example.milobeats.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun searchTracks(query: String): Flow<List<Track>>
    suspend fun getTrackDetails(trackId: String): Flow<Track>
    suspend fun getTrackStreamUrl(trackId: String): Flow<String>
    suspend fun getRelatedTracks(trackId: String): Flow<List<Track>>
    fun getSource(): MusicSource
} 