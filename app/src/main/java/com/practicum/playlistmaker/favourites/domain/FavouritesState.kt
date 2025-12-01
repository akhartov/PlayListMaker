package com.practicum.playlistmaker.favourites.domain

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface FavouritesState {
    data object Loading : FavouritesState
    data class Content(val tracks: List<Track>) : FavouritesState
    data object Empty: FavouritesState
}