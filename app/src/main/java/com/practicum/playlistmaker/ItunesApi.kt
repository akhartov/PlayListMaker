package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query



class ItunesResponse(
    @SerializedName("resultCount") val count: Int,
    @SerializedName("results") val tracks: ArrayList<Track>
)

interface ItunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ItunesResponse>

    companion object {
        const val HOST_URL = "https://itunes.apple.com"
    }
}