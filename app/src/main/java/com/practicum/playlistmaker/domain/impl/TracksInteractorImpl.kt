package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.repository.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TrackConsumer) {
        executor.execute {
            try {
                val tracks = repository.searchTracks(expression)
                consumer.consume(tracks)
            } catch (e: Exception) {
                consumer.fail(e)
            }
        }
    }
}