package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SearchTracksUseCase
import com.practicum.playlistmaker.domain.repository.TracksRepository
import java.util.concurrent.Executors

class SearchTracksUseCaseImpl(private val repository: TracksRepository) : SearchTracksUseCase {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: SearchTracksUseCase.TracksConsumer) {
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