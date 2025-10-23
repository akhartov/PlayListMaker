package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.api.AudioPlayer
import com.practicum.playlistmaker.player.domain.api.TrackPlayingState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(track: Track?, private val player: AudioPlayer) : ViewModel() {
    private var timerJob: Job? = null
    private val stateLiveData = MutableLiveData<PlayerState>(PlayerState.Stopped())
    fun getStateLiveData(): LiveData<PlayerState> = stateLiveData

    private val trackLiveData = MutableLiveData(track)
    fun getTrackLiveData(): LiveData<Track?> = trackLiveData

    private val playingObserver = Observer<TrackPlayingState> { state ->
        stateLiveData.postValue(PlayerState.Stopped())
        timerJob?.cancel()
    }

    init {
        track?.previewUrl?.let { player.open(it) }
        player.observeTrackPlayingState().observeForever(playingObserver)
    }

    override fun onCleared() {
        super.onCleared()
        player.observeTrackPlayingState().removeObserver(playingObserver)
    }

    fun clickPlay() {
        if (player.isPlaying()) {
            pause()
        } else
            play()
    }

    fun addCurrentTrackToFavourites() {
        //TODO: implement in a feature sprint
    }

    fun likeCurrentTrack() {
        //TODO: implement in a feature sprint
    }

    fun pause() {
        player.pause()
        timerJob?.cancel()
        stateLiveData.postValue(PlayerState.Paused(player.getCurrentPosition()))
    }

    private fun play() {
        player.play()
        startTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (player.isPlaying()) {
                stateLiveData.postValue(PlayerState.Playing(player.getCurrentPosition()))
                delay(300L)
            }
        }
    }
}