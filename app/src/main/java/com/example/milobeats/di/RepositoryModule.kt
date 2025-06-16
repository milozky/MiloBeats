package com.example.milobeats.di

import com.example.milobeats.data.repository.YouTubeRepositoryImpl
import com.example.milobeats.domain.repository.YouTubeRepository
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
    abstract fun bindYouTubeRepository(
        youTubeRepositoryImpl: YouTubeRepositoryImpl
    ): YouTubeRepository
} 