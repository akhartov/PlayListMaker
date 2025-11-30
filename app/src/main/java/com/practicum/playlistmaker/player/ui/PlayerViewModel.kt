package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favourites.domain.FavouritesInteractor
import com.practicum.playlistmaker.player.domain.api.AudioPlayer
import com.practicum.playlistmaker.player.domain.api.TrackPlayingState
import com.practicum.playlistmaker.playlist.domain.CoversInteractor
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    val track: Track?,
    private val player: AudioPlayer,
    private val trackFavouritesInteractor: FavouritesInteractor,
    private val coversInteractor: CoversInteractor,
    //private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private var timerJob: Job? = null
    private val stateLiveData = MutableLiveData<PlayerState>(PlayerState.Loaded(track))
    fun getStateLiveData(): LiveData<PlayerState> = stateLiveData

    private val _playlistsLiveData = MutableLiveData<List<PlaylistCover>>()
    val playlistsLiveData: LiveData<List<PlaylistCover>> = _playlistsLiveData

    private val userTrackState = MutableLiveData<UserTrackState?>(null)
    fun getUserTrackLiveData(): LiveData<UserTrackState?> = userTrackState
    private val playingObserver = Observer<TrackPlayingState> { state ->
        stateLiveData.postValue(PlayerState.Stopped(track))
        timerJob?.cancel()
    }

    init {
        track?.previewUrl?.let { player.open(it) }
        player.observeTrackPlayingState().observeForever(playingObserver)

        track?.let {
            viewModelScope.launch {
                trackFavouritesInteractor.beginMonitoring(track.trackId)
                trackFavouritesInteractor.flowTrackChanges().collect { state ->
                    userTrackState.postValue(state)
                }
            }
        }

        viewModelScope.launch {
            coversInteractor.subscribeToCoversFlow().collect { state ->
                _playlistsLiveData.postValue(state.items)
            }
        }
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

    fun tapFavouriteTrack() {
        track?.let {
            viewModelScope.launch {
                trackFavouritesInteractor.changeFavourite(track)
            }
        }
    }

    fun addTrackToPlaylist(playlistId: Int) {
        track?.let { existingTrack ->
            viewModelScope.launch {
                coversInteractor.addTrack(playlistId, existingTrack)
            }
        }
    }

    fun pause() {
        player.pause()
        timerJob?.cancel()
        stateLiveData.postValue(PlayerState.Paused(track, player.getCurrentPosition()))
    }

    private fun play() {
        player.play()
        startTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (player.isPlaying()) {
                stateLiveData.postValue(PlayerState.Playing(track, player.getCurrentPosition()))
                delay(300L)
            }
        }
    }
}