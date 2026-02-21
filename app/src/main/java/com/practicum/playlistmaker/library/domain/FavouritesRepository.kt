package com.practicum.playlistmaker.library.domain

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    suspend fun getAllTracks(): Flow<List<Track>>
    suspend fun getAllTracksIds(): Flow<List<Int>>
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(trackId: Int)
}