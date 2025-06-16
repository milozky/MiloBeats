package com.example.milobeats.di

import com.example.milobeats.domain.usecase.SearchYouTubeVideosUseCase
import com.example.milobeats.domain.usecase.SearchYouTubeVideosUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    abstract fun bindSearchYouTubeVideosUseCase(
        searchYouTubeVideosUseCaseImpl: SearchYouTubeVideosUseCaseImpl
    ): SearchYouTubeVideosUseCase
} 