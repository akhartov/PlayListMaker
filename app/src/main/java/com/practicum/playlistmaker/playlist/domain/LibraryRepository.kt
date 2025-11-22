package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    suspend fun addTrack(playlistId: Int, track: Track)
    suspend fun getTracksIds(playlistId: Int): List<Int>
    fun getTracks(playlistId: Int): Flow<List<TrackShortInfo>>
    suspend fun getTracksLength(playlistId: Int): Long
    suspend fun getTracksCount(playlistId: Int): Long
}