package com.practicum.playlistmaker.root.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.CoversInteractor
import com.practicum.playlistmaker.playlist.domain.model.PlaylistsEvent
import com.practicum.playlistmaker.ui.SingleLiveEvent
import kotlinx.coroutines.launch

class RootViewModel(
    private val coversInteractor: CoversInteractor
): ViewModel() {

    private val _stateliveData = SingleLiveEvent<PlaylistsEvent>()
    val stateliveData: LiveData<PlaylistsEvent> = _stateliveData

    init {
        viewModelScope.launch {
            coversInteractor.coverEventFlow.collect { state ->
                _stateliveData.postValue(state)
            }
        }
    }
}