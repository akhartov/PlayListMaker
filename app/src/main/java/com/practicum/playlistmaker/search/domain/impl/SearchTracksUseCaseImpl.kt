package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.ItunesResponse
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.domain.model.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.model.TrackMapper
import java.util.concurrent.Executors

class SearchTracksUseCaseImpl(private val networkClient: NetworkClient) : SearchTracksUseCase {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: SearchTracksUseCase.TracksConsumer) {
        executor.execute {
            try {
                val response = networkClient.doRequest(TracksSearchRequest(expression))
                if (response.resultCode == 200) {
                    val tracks = (response as ItunesResponse).results
                    consumer.consume(TrackMapper.mapToTrack(tracks))
                } else {
                    consumer.consume(emptyList())
                }
            } catch (e: Exception) {
                consumer.fail(e)
            }
        }
    }

}