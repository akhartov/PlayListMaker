package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import com.practicum.playlistmaker.search.domain.model.Track

interface PlaylistMessagingCache {
    fun hasTracks(): Boolean
    fun makeMessage(): String
    fun update(newCover: PlaylistCover)
    fun update(newTracks: List<Track>)
}