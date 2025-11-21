package com.practicum.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    val playlistInteractor: PlaylistInteractor
): ViewModel() {
    private val _playlistsLiveData = MutableLiveData<PlaylistsState>()
    val playlistLiveData: LiveData<PlaylistsState> = _playlistsLiveData

    init {
        viewModelScope.launch {
            playlistInteractor.playlistsFlow.collect { state ->
                _playlistsLiveData.postValue(state)
            }
        }

        viewModelScope.launch {
            playlistInteractor.update()
        }
    }
}