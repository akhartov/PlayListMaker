package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.ItunesClient
import com.practicum.playlistmaker.search.data.dto.ItunesResponse
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItunesClientImpl(private val service: ItunesApiService) : ItunesClient {
    override suspend fun doRequest(request: TracksSearchRequest): ItunesResponse {
        return withContext(Dispatchers.IO) {
            service.search(request.expression)
        }
    }
}