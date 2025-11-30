package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksLibraryRepository {
    suspend fun addTrack(playlistId: Int, track: Track)
    suspend fun getTracksIds(playlistId: Int): List<Int>
    suspend fun getTracksLength(playlistId: Int): Long
    suspend fun getTracksCount(playlistId: Int): Long
    suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>>
    suspend fun deleteTrack(playlistId: Int, trackId: Int)
}