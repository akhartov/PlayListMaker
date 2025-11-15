package com.practicum.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistCover
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    val playlistInteractor: PlaylistInteractor
): ViewModel() {
    private val _playlistsLiveData = MutableLiveData<List<PlaylistCover>>()
    val playlistLiveData: LiveData<List<PlaylistCover>> = _playlistsLiveData

    init {
        viewModelScope.launch {
            playlistInteractor.playlistsFlow.collect { items ->
                _playlistsLiveData.postValue(items)
            }
        }

        viewModelScope.launch {
            playlistInteractor.update()
        }
    }
}