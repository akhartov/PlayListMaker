package com.practicum.playlistmaker.search.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.repository.HistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class HistoryRepositoryImpl(context: Context) : HistoryRepository {
    private val storage = context.getSharedPreferences("SEARCH", Context.MODE_PRIVATE)
    private val listDataType = object : TypeToken<List<Track>>() {}.type
    private val gson = Gson()

    companion object {
        private const val TRACKS_FIELD = "TRACKS"
    }

    override fun saveTracks(tracks: List<Track>) {
        val serializedTracks = gson.toJson(tracks)
        storage.edit().putString(TRACKS_FIELD, serializedTracks).apply()
    }

    override fun getTracks(): MutableList<Track> {
        val tracksString = storage.getString(TRACKS_FIELD, null) ?: return mutableListOf()
        return gson.fromJson(tracksString, listDataType)
    }

    override fun clearTracks() {
        storage.edit().clear().apply()
    }
}