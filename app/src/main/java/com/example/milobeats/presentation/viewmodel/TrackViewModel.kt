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
} 