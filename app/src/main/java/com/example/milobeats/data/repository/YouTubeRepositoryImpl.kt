package com.example.milobeats.data.repository

import android.util.Log
import com.example.milobeats.data.api.YouTubeApiService
import com.example.milobeats.data.api.VideoItem
import com.example.milobeats.domain.repository.YouTubeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class YouTubeRepositoryImpl @Inject constructor(
    private val apiService: YouTubeApiService,
    private val apiKey: String
) : YouTubeRepository {

    override suspend fun searchVideos(query: String): Flow<List<VideoItem>> = flow {
        try {
            val response = apiService.searchVideos(
                query = query,
                apiKey = apiKey
            )
            Log.d("YouTubeRepository", "Search results: ${response.items.size} videos")
            emit(response.items)
        } catch (e: Exception) {
            Log.e("YouTubeRepository", "Error searching videos", e)
            emit(emptyList())
        }
    }

    override suspend fun getVideoDetails(videoId: String): Flow<VideoItem> = flow {
        try {
            val response = apiService.searchVideos(
                query = videoId,
                apiKey = apiKey
            )
            val video = response.items.firstOrNull()
            if (video != null) {
                emit(video)
            } else {
                throw Exception("Video not found")
            }
        } catch (e: Exception) {
            Log.e("YouTubeRepository", "Error getting video details", e)
            throw e
        }
    }
} 