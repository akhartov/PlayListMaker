package com.practicum.playlistmaker.player.domain.model

import com.practicum.playlistmaker.domain.models.Track

interface TrackDisplayInteractor {
    fun show(track: Track)
}