package com.example.milobeats.domain.usecase

import com.example.milobeats.data.model.Track
import com.example.milobeats.data.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTracksUseCase @Inject constructor(
    private val repository: TrackRepository
) {
    suspend operator fun invoke(query: String): Flow<List<Track>> {
        return repository.searchTracks(query)
    }
} 