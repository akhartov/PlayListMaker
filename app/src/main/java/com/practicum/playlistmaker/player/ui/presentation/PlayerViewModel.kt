package com.practicum.playlistmaker.player.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favourites.domain.FavouritesInteractor
import com.practicum.playlistmaker.playlist.domain.CoversInteractor
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class PlayerViewModel(
    val track: Track?,
    private val trackFavouritesInteractor: FavouritesInteractor,
    private val coversInteractor: CoversInteractor,
) : ViewModel() {
    private var audioPlayerControl: AudioPlayerControl? = null
    private val _trackStateLiveData = MutableLiveData(TrackState(track))
    fun getTrackStateLiveData(): LiveData<TrackState> = _trackStateLiveData

    private val _playlistsLiveData = MutableLiveData<List<PlaylistCover>>()
    val playlistsLiveData: LiveData<List<PlaylistCover>> = _playlistsLiveData

    private val userTrackState = MutableLiveData<UserTrackState?>(null)
    fun getUserTrackLiveData(): LiveData<UserTrackState?> = userTrackState

    init {

        track?.let {
            viewModelScope.launch {
                trackFavouritesInteractor.beginMonitoring(track.trackId)
                trackFavouritesInteractor.flowTrackChanges().collect { state ->
                    userTrackState.postValue(state)
                }
            }
        }

        viewModelScope.launch {
            coversInteractor.getAllCoversFlow().collect { state ->
                _playlistsLiveData.postValue(state.items)
            }
        }
    }

    fun clickPlay() {
        if (playerState.value is PlayerState.Playing) {
            audioPlayerControl?.pausePlayer()
        } else {
            audioPlayerControl?.startPlayer()
        }
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

    fun onViewPaused() {
        audioPlayerControl?.moveToForeground()
    }

    fun onViewResumed() {
        audioPlayerControl?.hideNotification()
    }

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect {
                playerState.postValue(it)
            }
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState
}