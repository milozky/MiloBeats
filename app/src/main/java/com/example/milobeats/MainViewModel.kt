package com.example.milobeats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milobeats.data.model.Track
import com.example.milobeats.domain.usecase.SearchTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Track>>(emptyList())
    val searchResults: StateFlow<List<Track>> = _searchResults.asStateFlow()

    private val _selectedTrack = MutableStateFlow<Track?>(null)
    val selectedTrack: StateFlow<Track?> = _selectedTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private var playbackJob: Job? = null

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchTracks(query: String) {
        viewModelScope.launch {
            searchTracksUseCase.execute(query)
                .catch { e ->
                    // Handle error, e.g., log it or show a message
                    _searchResults.value = emptyList()
                }
                .collectLatest { tracks ->
                    _searchResults.value = tracks
                }
        }
    }

    fun selectTrack(track: Track) {
        _selectedTrack.value = track
        // Reset playback state when selecting a new track
        stopPlayback()
        _currentPosition.value = 0L
        _duration.value = 0L
        _isPlaying.value = false
    }

    fun togglePlayback() {
        if (_isPlaying.value) {
            pausePlayback()
        } else {
            startPlayback()
        }
    }

    private fun startPlayback() {
        _isPlaying.value = true
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (true) {
                delay(1000) // Update every second
                if (_isPlaying.value) {
                    _currentPosition.value += 1000
                    if (_currentPosition.value >= _duration.value) {
                        stopPlayback()
                    }
                }
            }
        }
    }

    private fun pausePlayback() {
        _isPlaying.value = false
        playbackJob?.cancel()
    }

    private fun stopPlayback() {
        _isPlaying.value = false
        _currentPosition.value = 0L
        playbackJob?.cancel()
    }

    fun seekTo(position: Long) {
        _currentPosition.value = position.coerceIn(0L, _duration.value)
    }

    fun setDuration(newDuration: Long) {
        _duration.value = newDuration
    }

    override fun onCleared() {
        super.onCleared()
        playbackJob?.cancel()
    }
} 