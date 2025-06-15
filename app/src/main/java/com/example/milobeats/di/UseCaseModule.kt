package com.example.milobeats.di

import com.example.milobeats.domain.usecase.SearchTracksUseCase
import com.example.milobeats.domain.usecase.SearchTracksUseCaseImpl
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
    abstract fun bindSearchTracksUseCase(
        searchTracksUseCaseImpl: SearchTracksUseCaseImpl
    ): SearchTracksUseCase
} 