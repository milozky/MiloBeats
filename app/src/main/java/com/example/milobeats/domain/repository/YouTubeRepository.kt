package com.example.milobeats.domain.repository

import com.example.milobeats.data.api.VideoItem
import kotlinx.coroutines.flow.Flow

interface YouTubeRepository {
    suspend fun searchVideos(query: String): Flow<List<VideoItem>>
    suspend fun getVideoDetails(videoId: String): Flow<VideoItem>
} 