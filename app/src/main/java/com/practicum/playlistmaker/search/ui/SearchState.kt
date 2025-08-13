package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.domain.models.Track

sealed interface SearchState {
    object Empty : SearchState
    object InProgress : SearchState
    object NotFound : SearchState
    data class Error(val message: String) : SearchState
    data class Found(val foundTracks: List<Track>) : SearchState
    data class History(val historyTracks: List<Track>) : SearchState
}