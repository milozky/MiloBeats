package com.example.milobeats.di

import com.example.milobeats.data.api.LastFmApiService
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
    fun provideGson() = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: GsonBuilder): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/")
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .build()
    }

    @Provides
    @Singleton
    fun provideLastFmApiService(retrofit: Retrofit): LastFmApiService {
        return retrofit.create(LastFmApiService::class.java)
    }
} 