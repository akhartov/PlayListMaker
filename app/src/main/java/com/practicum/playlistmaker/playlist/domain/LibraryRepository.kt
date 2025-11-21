package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.search.domain.model.Track

interface LibraryRepository {
    suspend fun addTrack(playlistId: Int, track: Track)
    suspend fun getTracksIds(playlistId: Int): List<Int>
}