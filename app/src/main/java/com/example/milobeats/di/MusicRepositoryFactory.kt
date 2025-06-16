package com.example.milobeats.di

import com.example.milobeats.domain.model.MusicSource
import com.example.milobeats.domain.repository.MusicRepository
import com.example.milobeats.data.repository.YouTubeMusicRepository
import javax.inject.Inject
import javax.inject.Provider

class MusicRepositoryFactory @Inject constructor(
    private val youtubeRepository: Provider<YouTubeMusicRepository>
    // Add more repository providers as needed
) {
    fun getRepository(source: MusicSource): MusicRepository {
        return when (source) {
            MusicSource.YOUTUBE -> youtubeRepository.get()
            // Add more cases as needed
            else -> throw IllegalArgumentException("Unsupported music source: $source")
        }
    }
} 