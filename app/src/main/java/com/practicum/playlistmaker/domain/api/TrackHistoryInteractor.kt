package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TrackHistoryInteractor {

    fun addTrack(track: Track)
    fun clear()

    interface ChangeListener {
        fun onChange(tracks: List<Track>)
        fun onClear()
    }
}