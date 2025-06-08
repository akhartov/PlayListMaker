package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchEngine(val stateListener: UiStateListener) {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesApi.HOST_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ItunesApi::class.java)

    private fun prepareRequestString(searchText: String): String {
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
    }
}