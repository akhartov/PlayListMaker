package com.practicum.playlistmaker.search.domain.model

interface TrackHistoryInteractor {
    fun addTrack(track: Track)
    fun clear()
    fun isEmpty(): Boolean
    fun getTracks(consumer: Consumer)

    interface Consumer {
        fun consume(tracks: List<Track>)
    }
}