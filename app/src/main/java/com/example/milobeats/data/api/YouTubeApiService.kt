package com.example.milobeats.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("youtube/v3/search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 30,
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("videoCategoryId") videoCategoryId: String = "10", // Music category
        @Query("key") apiKey: String
    ): YouTubeSearchResponse
}

data class YouTubeSearchResponse(
    val items: List<VideoItem>,
    val nextPageToken: String?
)

data class VideoItem(
    val id: VideoId,
    val snippet: VideoSnippet
)

data class VideoId(
    val videoId: String
)

data class VideoSnippet(
    val title: String,
    val channelTitle: String,
    val thumbnails: Thumbnails,
    val publishedAt: String
)

data class Thumbnails(
    val default: Thumbnail,
    val medium: Thumbnail,
    val high: Thumbnail
)

data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
) 