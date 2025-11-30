package com.practicum.playlistmaker.playlist.ui.grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.CoversInteractor
import com.practicum.playlistmaker.playlist.domain.model.CoverLibraryState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val coversInteractor: CoversInteractor,
): ViewModel() {
    private val _coverLibraryLiveData = MutableLiveData<CoverLibraryState>()
    val coverLibraryLiveData: LiveData<CoverLibraryState> = _coverLibraryLiveData

    init {
        viewModelScope.launch {
            coversInteractor.subscribeToCoversFlow().collect { state ->
                _coverLibraryLiveData.postValue(state)
            }
        }
    }
}