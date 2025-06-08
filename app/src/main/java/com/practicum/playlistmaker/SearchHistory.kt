package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(val storage: SharedPreferences, val changeListener: OnTracksChangeListener) {
    private val listDataType = object : TypeToken<List<Track>>() {}.type
    private val gson = Gson()

    companion object {
        private val TRACKS_FIELD = "TRACKS"
    }

    init {
        changeListener.onChange(getTracks())
    }

    private fun getTracks(): MutableList<Track> {
        val tracksString = storage.getString(TRACKS_FIELD, null) ?: return mutableListOf()
        return gson.fromJson(tracksString, listDataType)
    }

    fun addTrack(track: Track) {
        var tracks = getTracks()
        tracks.add(0, track)
        tracks = tracks.toSet().toMutableList()
        if (tracks.size > 10)
            tracks.remove(tracks[tracks.size - 1])

        val serializedTracks = gson.toJson(tracks)
        storage.edit().putString(TRACKS_FIELD, serializedTracks).apply()
        changeListener.onChange(tracks)
    }

    fun clear() {
        storage.edit().clear().apply()
        changeListener.onClear()
    }
}