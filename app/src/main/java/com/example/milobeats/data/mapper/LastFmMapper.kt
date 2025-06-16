package com.example.milobeats.data.mapper

import com.example.milobeats.data.api.LastFmImage
import com.example.milobeats.data.api.LastFmTrack
import com.example.milobeats.data.api.LastFmTrackInfo
import com.example.milobeats.domain.model.Track

object LastFmMapper {

    fun LastFmTrack.toTrack(): Track {
        val thumbnailUrl = this.image?.let { images ->
            images.firstOrNull { it.size == "large" || it.size == "medium" }?.text
        } ?: ""

        return Track(
            id = this.mbid ?: (this.name + this.artist).hashCode().toString(),
            title = this.name,
            artist = this.artist,
            thumbnailUrl = thumbnailUrl,
            streamUrl = this.url // Last.fm provides a URL to the track page, not a direct stream
        )
    }

    fun List<LastFmTrack>.toTracks(): List<Track> {
        return this.map { it.toTrack() }
    }

    fun LastFmTrackInfo.toTrack(): Track {
        val thumbnailUrl = this.album?.image?.let { images ->
            images.firstOrNull { it.size == "large" || it.size == "medium" }?.text
        } ?: ""

        return Track(
            id = this.mbid ?: (this.name + this.artist.name).hashCode().toString(),
            title = this.name,
            artist = this.artist.name,
            thumbnailUrl = thumbnailUrl,
            streamUrl = this.url,
            duration = this.duration?.toLongOrNull() ?: 0L,
            listeners = this.listeners?.toLongOrNull() ?: 0L,
            playcount = this.playcount?.toLongOrNull() ?: 0L
        )
    }

    private fun List<LastFmImage>.findOptimalThumbnailUrl(): String? {
        val imageSizes = listOf("mega", "extralarge", "large", "medium", "small")
        for (size in imageSizes) {
            val imageUrl = this.firstOrNull { it.size == size }?.text
            if (!imageUrl.isNullOrEmpty()) {
                return imageUrl
            }
        }
        return null
    }
} 