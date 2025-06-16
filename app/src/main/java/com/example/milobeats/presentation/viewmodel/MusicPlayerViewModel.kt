package com.example.milobeats.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milobeats.di.MusicRepositoryFactory
import com.example.milobeats.domain.model.MusicSource
import com.example.milobeats.domain.model.Track
import com.example.milobeats.domain.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val repositoryFactory: MusicRepositoryFactory,
    private val application: Application
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Track>>(emptyList())
    val searchResults: StateFlow<List<Track>> = _searchResults.asStateFlow()

    private val _selectedTrack = MutableStateFlow<Track?>(null)
    val selectedTrack: StateFlow<Track?> = _selectedTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private var currentRepository: MusicRepository = repositoryFactory.getRepository(MusicSource.YOUTUBE)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchTracks(query: String) {
        viewModelScope.launch {
            currentRepository.searchTracks(query).collect { tracks ->
                _searchResults.value = tracks
            }
        }
    }

    fun selectTrack(track: Track) {
        _selectedTrack.value = track
        // If the track is from a different source, switch repositories
        if (track.source != currentRepository.getSource()) {
            currentRepository = repositoryFactory.getRepository(track.source)
        }
    }

    fun togglePlayback() {
        _isPlaying.value = !_isPlaying.value
    }

    fun getSource(): MusicSource {
        return currentRepository.getSource()
    }
} 