package com.example.milobeats.domain.model

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val thumbnailUrl: String,
    val source: MusicSource,
    val sourceId: String, // Original ID from the source (e.g., YouTube video ID, Spotify track ID)
    val duration: Long? = null,
    val album: String? = null,
    val releaseDate: String? = null
)

enum class MusicSource {
    YOUTUBE,
    SPOTIFY,
    LASTFM,
    // Add more sources as needed
} 