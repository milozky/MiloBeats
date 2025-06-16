package com.example.milobeats.data.mapper

import com.example.milobeats.data.api.VideoItem
import com.example.milobeats.domain.model.MusicSource
import com.example.milobeats.domain.model.Track

object YouTubeMapper {
    fun VideoItem.toTrack(): Track {
        return Track(
            id = id.videoId,
            title = snippet.title,
            artist = snippet.channelTitle,
            thumbnailUrl = snippet.thumbnails.medium.url,
            source = MusicSource.YOUTUBE,
            sourceId = id.videoId,
            releaseDate = snippet.publishedAt
        )
    }

    fun List<VideoItem>.toTracks(): List<Track> {
        return map { it.toTrack() }
    }
} 