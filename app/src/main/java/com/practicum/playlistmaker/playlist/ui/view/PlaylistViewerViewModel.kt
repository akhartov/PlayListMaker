package com.practicum.playlistmaker.playlist.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistCover
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistViewerState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class PlaylistViewerViewModel(
    val cover: PlaylistCover?,
    val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistViewerState>()
    val stateLiveData: LiveData<PlaylistViewerState> = _stateLiveData

    private val _tracksLiveData = MutableLiveData<List<Track>>()
    val tracksLiveData: LiveData<List<Track>> = _tracksLiveData

    init {
        viewModelScope.launch {
            playlistInteractor.getPlaylistViewerFlow(cover).collect { state ->
                _stateLiveData.postValue(state)
            }
        }

        cover?.let {
            viewModelScope.launch {
                playlistInteractor.getTracksFlow(cover.id).collect {
                    _tracksLiveData.postValue(it)
                }
            }
        }
    }


    fun deleteTrack(track: Track) {
        cover?.let {
            viewModelScope.launch {
                playlistInteractor.deleteTrack(cover.id, track)
            }
        }
    }
}