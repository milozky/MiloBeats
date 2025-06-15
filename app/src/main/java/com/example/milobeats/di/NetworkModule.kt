package com.example.milobeats.di

import com.example.milobeats.LastFmApiService
import com.example.milobeats.data.model.TrackSearchResponse
import com.example.milobeats.data.util.TrackSearchResponseTypeAdapter
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
        .registerTypeAdapter(TrackSearchResponse::class.java, TrackSearchResponseTypeAdapter())
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: com.google.gson.Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideLastFmApiService(retrofit: Retrofit): LastFmApiService {
        return retrofit.create(LastFmApiService::class.java)
    }
} 