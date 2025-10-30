package com.practicum.playlistmaker.library.domain

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface FavouritesState {
    object Loading : FavouritesState
    class Content(val tracks: List<Track>) : FavouritesState
    object Empty: FavouritesState
}