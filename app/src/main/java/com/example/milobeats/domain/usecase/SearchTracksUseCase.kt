package com.example.milobeats.domain.usecase

import com.example.milobeats.data.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksUseCase {
    suspend fun execute(query: String): Flow<List<Track>>
} 