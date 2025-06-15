package com.example.milobeats.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milobeats.data.model.Track
import com.example.milobeats.domain.usecase.SearchTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedTrack = MutableStateFlow<Track?>(null)
    val selectedTrack: StateFlow<Track?> = _selectedTrack

    private val _currentScreen = MutableStateFlow(0) // 0: Play, 1: Search, 2: Home
    val currentScreen: StateFlow<Int> = _currentScreen

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    init {
        viewModelScope.launch {
            searchTracks("") // Initial search when ViewModel is created
        }
    }

    fun searchTracks(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            searchTracksUseCase.execute(query)
                .catch { e ->
                    _error.value = e.message
                    _isLoading.value = false
                    Log.e("TrackViewModel", "Error searching tracks: ${e.message}", e)
                }
                .collect { tracks ->
                    _tracks.value = tracks
                    _isLoading.value = false
                    Log.d("TrackViewModel", "Tracks collected: ${tracks.size} tracks")
                }
        }
    }

    fun selectTrack(track: Track) {
        _selectedTrack.value = track
        _currentScreen.value = 0 // Navigate to play screen
        // Reset playback state
        _isPlaying.value = false
        _currentPosition.value = 0L
        _duration.value = 0L
    }

    fun setCurrentScreen(screenIndex: Int) {
        _currentScreen.value = screenIndex
    }

    fun togglePlayback() {
        _isPlaying.value = !_isPlaying.value
    }

    fun updatePosition(position: Long) {
        _currentPosition.value = position
    }

    fun updateDuration(newDuration: Long) {
        _duration.value = newDuration
    }

    fun seekTo(position: Long) {
        _currentPosition.value = position.coerceIn(0, _duration.value)
    }
} 