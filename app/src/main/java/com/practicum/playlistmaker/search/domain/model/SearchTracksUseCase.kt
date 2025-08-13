package com.practicum.playlistmaker.search.domain.model

import java.lang.Exception

interface SearchTracksUseCase {
    fun search(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(tracks: List<Track>)
        fun fail(e: Exception)
    }
}