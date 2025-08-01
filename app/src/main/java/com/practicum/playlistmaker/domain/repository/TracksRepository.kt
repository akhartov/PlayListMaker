package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}