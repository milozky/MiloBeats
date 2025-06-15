package com.example.milobeats.domain.usecase

import com.example.milobeats.data.model.Track
import com.example.milobeats.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTracksUseCaseImpl @Inject constructor(
    private val repository: TrackRepository
) : SearchTracksUseCase {
    override suspend fun execute(query: String): Flow<List<Track>> {
        return repository.searchTracks(query)
    }
} 