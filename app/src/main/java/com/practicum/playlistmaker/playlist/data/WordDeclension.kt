package com.practicum.playlistmaker.playlist.data

import android.content.Context
import com.practicum.playlistmaker.R

class WordDeclension(val context: Context) {
    fun getTrackString(number: Int): String {
        return context.resources.getQuantityString(R.plurals.track_count, number, number)
    }

    fun getMinutesString(number: Int): String {
        return context.resources.getQuantityString(R.plurals.minute_count, number, number)
    }
}
