package com.example.milobeats.data.repository

import com.example.milobeats.data.api.YouTubeApiService
import com.example.milobeats.data.mapper.YouTubeMapper.toTrack
import com.example.milobeats.data.mapper.YouTubeMapper.toTracks
import com.example.milobeats.domain.model.MusicSource
import com.example.milobeats.domain.model.Track
import com.example.milobeats.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.example.milobeats.BuildConfig

class YouTubeMusicRepository @Inject constructor(
    private val apiService: YouTubeApiService
) : MusicRepository {

    private val apiKey: String = BuildConfig.YOUTUBE_API_KEY

    override fun getSource(): MusicSource = MusicSource.YOUTUBE

    override suspend fun searchTracks(query: String): Flow<List<Track>> = flow {
        try {
            val response = apiService.searchVideos(
                query = query,
                apiKey = apiKey
            )
            emit(response.items.toTracks())
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getTrackDetails(trackId: String): Flow<Track> = flow {
        try {
            val response = apiService.searchVideos(
                query = trackId,
                apiKey = apiKey
            )
            val video = response.items.firstOrNull()
            if (video != null) {
                emit(video.toTrack())
            } else {
                throw Exception("Track not found")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getTrackStreamUrl(trackId: String): Flow<String> = flow {
        // YouTube doesn't provide direct stream URLs, so we return the video ID
        emit(trackId)
    }

    override suspend fun getRelatedTracks(trackId: String): Flow<List<Track>> = flow {
        try {
            val response = apiService.searchVideos(
                query = trackId,
                apiKey = apiKey
            )
            emit(response.items.toTracks())
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
} 