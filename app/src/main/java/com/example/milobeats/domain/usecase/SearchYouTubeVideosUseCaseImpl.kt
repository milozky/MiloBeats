package com.example.milobeats.domain.usecase

import com.example.milobeats.data.api.VideoItem
import com.example.milobeats.domain.repository.YouTubeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchYouTubeVideosUseCaseImpl @Inject constructor(
    private val repository: YouTubeRepository
) : SearchYouTubeVideosUseCase {
    override suspend fun execute(query: String): Flow<List<VideoItem>> {
        return repository.searchVideos(query)
    }
} 