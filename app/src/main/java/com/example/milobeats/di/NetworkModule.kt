package com.example.milobeats.di

import com.example.milobeats.BuildConfig
import com.example.milobeats.data.api.YouTubeApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): com.google.gson.Gson = GsonBuilder()
        .create()

    @Provides
    @Singleton
    fun provideYouTubeApiService(): YouTubeApiService {
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YouTubeApiService::class.java)
    }
} 