package com.example.milobeats.domain.usecase

import com.example.milobeats.data.api.VideoItem
import kotlinx.coroutines.flow.Flow

interface SearchYouTubeVideosUseCase {
    suspend fun execute(query: String): Flow<List<VideoItem>>
} 