package com.example.milobeats.domain.usecase

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.extractor.mp4.Track
import com.example.milobeats.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTracksUseCaseImpl @Inject constructor(
    private val repository: TrackRepository
) : SearchTracksUseCase {
    @OptIn(UnstableApi::class)
    override suspend fun execute(query: String): Flow<List<Track>> {
        return repository.searchTracks(query)
    }
} 