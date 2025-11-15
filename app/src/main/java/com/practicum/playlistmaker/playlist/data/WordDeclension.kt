package com.practicum.playlistmaker.playlist.data

import android.content.Context
import com.practicum.playlistmaker.R

class WordDeclension(val context: Context) {
    private val forms = arrayOf(
        context.resources.getString(R.string.word_track),
        context.resources.getString(R.string.word_tracka),
        context.resources.getString(R.string.word_trackov)
    )

    fun getTrackString(number: Int): String {
        val lastDigit = number % 10
        val lastTwoDigits = number % 100

        return when {
            (lastTwoDigits in 11..14) -> forms[2]
            lastDigit == 1 -> forms[0]
            lastDigit in 2..4 -> forms[1]
            else -> forms[2]
        }
    }
}
