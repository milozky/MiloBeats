package com.example.milobeats.di

import com.example.milobeats.data.repository.YouTubeMusicRepository
import com.example.milobeats.domain.repository.MusicRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        youTubeMusicRepository: YouTubeMusicRepository
    ): MusicRepository
} 