package com.practicum.playlistmaker.favourites.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favourites.domain.FavouritesInteractor
import com.practicum.playlistmaker.favourites.domain.FavouritesState
import kotlinx.coroutines.launch

class FavouritesTracksViewModel(
    private val trackFavouritesInteractor: FavouritesInteractor
) : ViewModel() {

    private val _stateLiveData = MutableLiveData(FavouritesState.Loading as FavouritesState)
    fun getStateLiveData(): LiveData<FavouritesState> = _stateLiveData

    fun start() {
        viewModelScope.launch {
            trackFavouritesInteractor
                .getFavouriteTracks()
                .collect { tracks ->
                    _stateLiveData.postValue(
                        if (tracks.isEmpty())
                            FavouritesState.Empty
                        else
                            FavouritesState.Content(tracks)
                    )
                }
        }
    }
}