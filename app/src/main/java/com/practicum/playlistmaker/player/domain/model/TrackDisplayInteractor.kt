package com.practicum.playlistmaker.player.domain.model

import com.practicum.playlistmaker.search.domain.model.Track

interface TrackDisplayInteractor {
    fun show(track: Track)
}