package com.example.milobeats.domain.repository


import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.extractor.mp4.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    @OptIn(UnstableApi::class)
    suspend fun searchTracks(query: String): Flow<List<Track>>

    @OptIn(UnstableApi::class)
    suspend fun getTrackDetails(trackId: String): Flow<Track>
} 