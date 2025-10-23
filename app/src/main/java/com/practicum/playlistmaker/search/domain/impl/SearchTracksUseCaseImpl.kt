package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.data.ItunesClient
import com.practicum.playlistmaker.search.domain.model.FoundTracksResponse
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.domain.model.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.model.TrackMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTracksUseCaseImpl(private val itunesClient: ItunesClient) : SearchTracksUseCase {
    override fun search(expression: String): Flow<FoundTracksResponse> = flow {
        emit(
            FoundTracksResponse(TrackMapper.mapToTrack(itunesClient.doRequest(TracksSearchRequest(expression)).results))
        )
    }

}