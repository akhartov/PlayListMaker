package com.practicum.playlistmaker.presentation.tracks

import com.practicum.playlistmaker.domain.models.Track

interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}