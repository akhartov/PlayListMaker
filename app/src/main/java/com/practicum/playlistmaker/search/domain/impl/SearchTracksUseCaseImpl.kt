package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.ItunesResponse
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.domain.api.SearchTracksUseCase
import java.util.concurrent.Executors

class SearchTracksUseCaseImpl(private val networkClient: NetworkClient) : SearchTracksUseCase {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: SearchTracksUseCase.TracksConsumer) {
        executor.execute {
            try {
                val response = networkClient.doRequest(TracksSearchRequest(expression))
                if (response.resultCode == 200) {
                    val tracks = (response as ItunesResponse).results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }

                    consumer.consume(tracks)
                } else {
                    consumer.consume(emptyList())
                }
            } catch (e: Exception) {
                consumer.fail(e)
            }
        }
    }

}