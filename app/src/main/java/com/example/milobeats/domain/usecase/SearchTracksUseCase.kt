package com.example.milobeats.domain.usecase

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.extractor.mp4.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksUseCase {
    @OptIn(UnstableApi::class)
    suspend fun execute(query: String): Flow<List<Track>>
} 