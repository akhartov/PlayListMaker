package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.ItunesResponse
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest

interface ItunesClient {
    suspend fun doRequest(request: TracksSearchRequest): ItunesResponse
}