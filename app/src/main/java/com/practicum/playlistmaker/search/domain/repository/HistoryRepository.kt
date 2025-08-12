package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun saveTracks(tracks: List<Track>)
    fun getTracks(): List<Track>
    fun clearTracks()
}