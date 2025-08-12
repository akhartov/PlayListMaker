package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.domain.models.Track

fun interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}