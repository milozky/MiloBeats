package com.example.milobeats

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milobeats.data.model.Track
import com.example.milobeats.domain.usecase.SearchTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _isMediaPlayerPrepared = MutableStateFlow(false)

    private var playbackJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    private val _reinitializeMediaPlayerEvent = MutableSharedFlow<Unit>()

    // Coroutine Exception Handler
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            "MainViewModel",
            "Caught unhandled coroutine exception: ${throwable.message}",
            throwable
        )
        // You can also update UI state to reflect an error here if needed
        _isPlaying.value = false
        stopPlayback()
    }

    init {
        initializeMediaPlayer()

        viewModelScope.launch(exceptionHandler) { // Apply exception handler
            _reinitializeMediaPlayerEvent.asSharedFlow().collect { _ ->
                Log.d("MainViewModel", "Re-initializing MediaPlayer due to event.")
                initializeMediaPlayer()
            }
        }
    }

    private fun initializeMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnCompletionListener { // Reset state on completion
            Log.d("MainViewModel", "Playback completed")
            _isPlaying.value = false
            _currentPosition.value = 0L
            playbackJob?.cancel()
        }
        mediaPlayer?.setOnErrorListener { mp, what, extra ->
            Log.e(
                "MainViewModel",
                "MediaPlayer error: what=$what, extra=$extra (Player state: ${if (mp.isPlaying) "Playing" else "Not Playing"})\n" + Log.getStackTraceString(
                    Throwable()
                )
            )
            if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                Log.e(
                    "MainViewModel",
                    "MEDIA_ERROR_SERVER_DIED: Media server died. Emitting re-initialization event."
                )
                viewModelScope.launch(exceptionHandler) { // Apply exception handler
                    _reinitializeMediaPlayerEvent.emit(Unit)
                }
            } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                Log.e("MainViewModel", "MEDIA_ERROR_UNKNOWN: Unknown error occurred.")
            } else if (what == -38) {
                Log.e(
                    "MainViewModel",
                    "MediaPlayer IllegalStateException or general error (what=-38). Likely due to incorrect state transition."
                )
            }
            stopPlayback() // Stop playback on any error
            true // Indicate that the error was handled
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchTracks(query: String) {
        viewModelScope.launch(exceptionHandler) { // Apply exception handler
            Log.d("MainViewModel", "Searching tracks for query: $query")
            searchTracksUseCase.execute(query)
                .catch { e ->
                    Log.e("MainViewModel", "Error searching tracks: ${e.message}", e)
                    _searchResults.value = emptyList()
                }
                .collectLatest { tracks ->
                    Log.d("MainViewModel", "Search results: ${tracks.size} tracks")
                    _searchResults.value = tracks
                }
        }
    }

    fun selectTrack(track: Track) {
        _selectedTrack.value = track
        Log.d("MainViewModel", "Selected track: ${track.name} by ${track.artist}")
        stopPlayback()
        _currentPosition.value = 0L
        _duration.value = 0L
        _isPlaying.value = false
        _isMediaPlayerPrepared.value = false

        // Use the track's URL from Last.fm
        val audioUrl = _selectedTrack.value?.url

        if (audioUrl == null) {
            Log.e("MainViewModel", "No URL available for track: ${_selectedTrack.value?.name}")
            _isPlaying.value = false
            return
        }

        Log.d("MainViewModel", "Attempting to play URL: $audioUrl")
        mediaPlayer?.apply {
            reset()
            Log.d("MainViewModel", "MediaPlayer state after reset (should be Idle).")
            try {
                setDataSource(audioUrl)
                Log.d(
                    "MainViewModel",
                    "DataSource set (should be Initialized). Calling prepareAsync()..."
                )
                prepareAsync() // Changed back to asynchronous prepareAsync()

                setOnPreparedListener { mp ->
                    Log.d(
                        "MainViewModel",
                        "MediaPlayer prepared (should be Prepared). Duration: ${mp.duration}"
                    )
                    _duration.value = mp.duration.toLong()
                    _isMediaPlayerPrepared.value = true
                    if (_isPlaying.value) {
                        viewModelScope.launch(exceptionHandler) { // Apply exception handler
                            delay(100) // Small delay to ensure state is fully updated
                            try {
                                mp.start()
                                startProgressUpdateJob()
                                Log.d(
                                    "MainViewModel",
                                    "Playback started after preparation (from onPrepared)."
                                )
                            } catch (e: IllegalStateException) {
                                Log.e(
                                    "MainViewModel",
                                    "IllegalStateException starting MediaPlayer after preparation: ${e.message}",
                                    e
                                )
                                _isPlaying.value = false
                                _currentPosition.value = 0L
                                _duration.value = 0L
                            } catch (e: Exception) {
                                Log.e(
                                    "MainViewModel",
                                    "General error starting MediaPlayer after preparation: ${e.message}",
                                    e
                                )
                                _isPlaying.value = false
                                _currentPosition.value = 0L
                                _duration.value = 0L
                            }
                        }
                    }
                }
            } catch (e: java.io.IOException) {
                Log.e(
                    "MainViewModel",
                    "IOException in setDataSource or prepareAsync: ${e.message}",
                    e
                )
                _isPlaying.value = false
                _currentPosition.value = 0L
                _duration.value = 0L
                _isMediaPlayerPrepared.value = false
            } catch (e: IllegalStateException) {
                Log.e(
                    "MainViewModel",
                    "IllegalStateException in setDataSource or prepareAsync: ${e.message}",
                    e
                )
                _isPlaying.value = false
                _currentPosition.value = 0L
                _duration.value = 0L
                _isMediaPlayerPrepared.value = false
            } catch (e: Exception) {
                Log.e(
                    "MainViewModel",
                    "General Exception in setDataSource or prepareAsync: ${e.message}",
                    e
                )
                _isPlaying.value = false
                _currentPosition.value = 0L
                _duration.value = 0L
                _isMediaPlayerPrepared.value = false
            }
        } ?: Log.e("MainViewModel", "MediaPlayer is null in selectTrack.")
    }

    fun togglePlayback() {
        if (_isPlaying.value) {
            pausePlayback()
        } else {
            _isPlaying.value = true
            mediaPlayer?.let { player ->
                if (_isMediaPlayerPrepared.value) {
                    if (!player.isPlaying) {
                        try {
                            player.start()
                            startProgressUpdateJob()
                            Log.d(
                                "MainViewModel",
                                "Playback started (immediately after toggle and prepared)."
                            )
                        } catch (e: IllegalStateException) {
                            Log.e(
                                "MainViewModel",
                                "IllegalStateException starting MediaPlayer from togglePlayback: ${e.message}",
                                e
                            )
                            _isPlaying.value = false
                            _currentPosition.value = 0L
                            _duration.value = 0L
                        } catch (e: Exception) {
                            Log.e(
                                "MainViewModel",
                                "General error starting MediaPlayer from togglePlayback: ${e.message}",
                                e
                            )
                            _isPlaying.value = false
                            _currentPosition.value = 0L
                            _duration.value = 0L
                        }
                    } else {

                    }
                } else {
                    Log.d(
                        "MainViewModel",
                        "Player not yet prepared. Waiting for onPreparedListener to start playback."
                    )
                }
            } ?: Log.e("MainViewModel", "MediaPlayer is null when togglePlayback called.")
        }
    }

    private fun startProgressUpdateJob() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch(exceptionHandler) { // Apply exception handler
            while (_isPlaying.value && mediaPlayer?.isPlaying == true) {
                _currentPosition.value = mediaPlayer?.currentPosition?.toLong() ?: 0L
                delay(1000)
            }
            // When the loop exits, ensure _isPlaying is false
            _isPlaying.value = false
            Log.d("MainViewModel", "Progress update job stopped.")
        }
    }

    private fun pausePlayback() {
        mediaPlayer?.pause()
        _isPlaying.value = false
        Log.d("MainViewModel", "Playback paused")
        playbackJob?.cancel()
    }

    private fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        _isPlaying.value = false
        _currentPosition.value = 0L
        _duration.value = 0L
        Log.d("MainViewModel", "Playback stopped and reset")
        playbackJob?.cancel()
        _isMediaPlayerPrepared.value = false
    }

    fun seekTo(position: Long) {
        if (_isMediaPlayerPrepared.value) {
            mediaPlayer?.seekTo(position.toInt())
            _currentPosition.value = position.coerceIn(0L, _duration.value)
            Log.d("MainViewModel", "Seeked to position: $position")
        } else {
            Log.d("MainViewModel", "Cannot seek, MediaPlayer not prepared.")
        }
    }

    fun setDuration(newDuration: Long) {
        _duration.value = newDuration
    }

    override fun onCleared() {
        super.onCleared()
        playbackJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d("MainViewModel", "MainViewModel cleared. MediaPlayer released.")
    }
} 