package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItunesNetworkClient(): NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesApiService.HOST_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ItunesApiService::class.java)

    /*private fun prepareRequestString(searchText: String): String {
        return searchText
    }

    fun search(dirtySearchText: String) {
        stateListener.onChange(Ui.InProgress)
        val searchText = prepareRequestString(dirtySearchText)
        if (searchText.isEmpty()) {
            stateListener.onChange(Ui.Empty)
            return
        }

        service.search(searchText).enqueue(
            object : Callback<ItunesResponse> {
                override fun onResponse(
                    call: Call<ItunesResponse>,
                    response: Response<ItunesResponse>
                ) {
                    val foundTracks = response.body()?.tracks
                    if (foundTracks.isNullOrEmpty()) {
                        stateListener.onChange(Ui.NotFound)
                    } else {
                        stateListener.onChange(Ui.Found(foundTracks))
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    stateListener.onChange(Ui.Error)
                }
            }
        )
    }*/

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val resp = service.search(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}