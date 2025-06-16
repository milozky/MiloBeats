package com.example.milobeats.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milobeats.data.api.VideoItem
import com.example.milobeats.domain.usecase.SearchYouTubeVideosUseCase
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YouTubePlayerViewModel @Inject constructor(
    private val searchVideosUseCase: SearchYouTubeVideosUseCase,
    private val application: Application
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<VideoItem>>(emptyList())
    val searchResults: StateFlow<List<VideoItem>> = _searchResults.asStateFlow()

    private val _selectedVideo = MutableStateFlow<VideoItem?>(null)
    val selectedVideo: StateFlow<VideoItem?> = _selectedVideo.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private var youTubePlayer: YouTubePlayer? = null

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchVideos(query: String) {
        viewModelScope.launch {
            searchVideosUseCase.execute(query).collect { videos ->
                _searchResults.value = videos
            }
        }
    }

    fun selectVideo(video: VideoItem) {
        _selectedVideo.value = video
        youTubePlayer?.loadVideo(video.id.videoId, 0f)
    }

    fun togglePlayback() {
        youTubePlayer?.let { player ->
            if (_isPlaying.value) {
                player.pause()
            } else {
                player.play()
            }
        }
    }

    fun onPlayerReady(player: YouTubePlayer) {
        youTubePlayer = player
        player.addListener(object : com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener() {
            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                _isPlaying.value = state == PlayerConstants.PlayerState.PLAYING
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        youTubePlayer = null
    }
} 