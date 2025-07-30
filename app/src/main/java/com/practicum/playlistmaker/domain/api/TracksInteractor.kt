package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track
import java.lang.Exception

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>)
        fun fail(e: Exception)
    }
}