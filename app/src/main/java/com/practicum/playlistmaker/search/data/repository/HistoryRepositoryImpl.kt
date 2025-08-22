package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.domain.repository.HistoryRepository
import com.practicum.playlistmaker.search.domain.model.Track

class HistoryRepositoryImpl(private val storage: SharedPreferences, private val gson: Gson) :
    HistoryRepository {
    private val listDataType = object : TypeToken<List<Track>>() {}.type

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

    companion object {
        private const val TRACKS_FIELD = "TRACKS"
    }
}