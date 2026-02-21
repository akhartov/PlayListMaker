package com.practicum.playlistmaker.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.FavouritesInteractor
import com.practicum.playlistmaker.playlist.domain.CoversInteractor
import com.practicum.playlistmaker.playlist.domain.model.CoverLibraryState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val trackFavouritesInteractor: FavouritesInteractor,
    private val coversInteractor: CoversInteractor,
) : ViewModel() {

    private val _tracksFlow = MutableStateFlow<List<Track>>(listOf())
    fun getTracksStateFlow(): StateFlow<List<Track>> = _tracksFlow.asStateFlow()

    private val _albumsFlow = MutableStateFlow(CoverLibraryState(listOf()))
    fun getAlbumsStateFlow(): StateFlow<CoverLibraryState> = _albumsFlow.asStateFlow()

    init {
        viewModelScope.launch {
            trackFavouritesInteractor.getFavouriteTracks()
                .collect { tracks ->
                    _tracksFlow.value = tracks
                }
        }

        viewModelScope.launch {
            coversInteractor.getAllCoversFlow().collect { state ->
                _albumsFlow.value = state
            }
        }
    }
}