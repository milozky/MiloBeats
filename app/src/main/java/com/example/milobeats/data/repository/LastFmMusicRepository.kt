package com.example.milobeats.data.repository

import com.example.milobeats.BuildConfig
import com.example.milobeats.data.api.LastFmApiService
import com.example.milobeats.data.mapper.LastFmMapper.toTrack
import com.example.milobeats.data.mapper.LastFmMapper.toTracks
import com.example.milobeats.domain.model.MusicSource
import com.example.milobeats.domain.model.Track
import com.example.milobeats.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LastFmMusicRepository @Inject constructor(
    private val apiService: LastFmApiService
) : MusicRepository {

    private val apiKey: String = BuildConfig.LASTFM_API_KEY

    override fun getSource(): MusicSource = MusicSource.LASTFM

    override suspend fun searchTracks(query: String): Flow<List<Track>> = flow {
        try {
            val response = apiService.searchTracks(
                track = query,
                apiKey = apiKey
            )
            emit(response.results.trackmatches.track.toTracks())
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getTrackDetails(trackId: String): Flow<Track> = flow {
        try {
            // Last.fm getInfo requires artist and track name, not just ID
            // For now, we'll assume the trackId is the track name for simplicity
            // In a real app, you might need to store more info or make another search
            val searchResponse = apiService.searchTracks(track = trackId, apiKey = apiKey)
            val lastFmTrack = searchResponse.results.trackmatches.track.firstOrNull()

            if (lastFmTrack != null) {
                val trackInfoResponse = apiService.getTrackInfo(
                    artist = lastFmTrack.artist,
                    track = lastFmTrack.name,
                    apiKey = apiKey
                )
                emit(trackInfoResponse.track.toTrack())
            } else {
                throw Exception("Track not found")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getTrackStreamUrl(trackId: String): Flow<String> = flow {
        // Last.fm does not provide direct stream URLs. We return the Last.fm track URL.
        // In a real app, you would integrate with a streaming service.
        val searchResponse = apiService.searchTracks(track = trackId, apiKey = apiKey)
        val lastFmTrack = searchResponse.results.trackmatches.track.firstOrNull()
        emit(lastFmTrack?.url ?: "")
    }

    override suspend fun getRelatedTracks(trackId: String): Flow<List<Track>> = flow {
        try {
            // Last.fm getSimilar requires artist and track name
            val searchResponse = apiService.searchTracks(track = trackId, apiKey = apiKey)
            val lastFmTrack = searchResponse.results.trackmatches.track.firstOrNull()

            if (lastFmTrack != null) {
                val similarResponse = apiService.getSimilarTracks(
                    artist = lastFmTrack.artist,
                    track = lastFmTrack.name,
                    apiKey = apiKey
                )
                emit(similarResponse.results.trackmatches.track.toTracks())
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
} 