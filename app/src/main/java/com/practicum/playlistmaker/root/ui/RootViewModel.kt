package com.practicum.playlistmaker.root.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistsEvent
import com.practicum.playlistmaker.ui.SingleLiveEvent
import kotlinx.coroutines.launch

class RootViewModel(val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val _stateliveData = SingleLiveEvent<PlaylistsEvent>()
    val stateliveData: LiveData<PlaylistsEvent> = _stateliveData

    init {
        viewModelScope.launch {
            playlistInteractor.playlistEventFlow.collect { state ->
                _stateliveData.postValue(state)
            }
        }
    }
}