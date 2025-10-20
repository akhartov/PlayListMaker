package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.ItunesResponse
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.domain.model.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.model.TrackMapper
import com.practicum.playlistmaker.search.ui.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTracksUseCaseImpl(private val networkClient: NetworkClient) : SearchTracksUseCase {
    override fun search(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                val tracks = (response as ItunesResponse).results
                emit(
                    Resource.Success(TrackMapper.mapToTrack(tracks))
                )
            }

            else -> emit(Resource.Error("Ошибка"))
        }
    }

}