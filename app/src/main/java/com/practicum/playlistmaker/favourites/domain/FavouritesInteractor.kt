package com.practicum.playlistmaker.favourites.domain

import com.practicum.playlistmaker.player.ui.presentation.UserTrackState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesInteractor {
    suspend fun changeFavourite(track: Track)
    suspend fun getFavouriteTracks(): Flow<List<Track>>
    suspend fun getFavouriteTracksIds(): Flow<List<Int>>
    suspend fun beginMonitoring(trackId: Int)
    suspend fun flowTrackChanges(): Flow<UserTrackState>
}