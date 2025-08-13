package com.practicum.playlistmaker.search.domain.model

import com.practicum.playlistmaker.domain.models.Track

interface TrackHistoryInteractor {
    fun addTrack(track: Track)
    fun clear()
    fun isEmpty(): Boolean
    fun getTracks(consumer: Consumer)

    interface Consumer {
        fun consume(tracks: List<Track>)
    }
}