package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ConnectException

class ItunesNetworkClient(private val service: ItunesApiService) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = service.search(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: ConnectException) {
                Response().apply { resultCode = -1 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }
}