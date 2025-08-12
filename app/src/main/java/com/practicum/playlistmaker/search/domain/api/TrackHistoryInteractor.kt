package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TrackHistoryInteractor {

    fun addTrack(track: Track)
    fun clear()
    fun isEmpty(): Boolean
    fun getTracks(): List<Track>

    interface ChangeListener {
        fun onChange(tracks: List<Track>)
        fun onClear()
    }
}