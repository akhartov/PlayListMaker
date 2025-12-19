package com.practicum.playlistmaker.player.ui.presentation

import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.model.millisToMMSS

sealed class PlayerState(
    val track: Track?,
    val isPlaying: Boolean? = null,
    val trackTimePosition: String? = null
) {
    class Loaded(track: Track?) :
        PlayerState(track, false, millisToMMSS(0))

    class Playing(track: Track?, position: Int) :
        PlayerState(track, true, millisToMMSS(position))

    class Paused(track: Track?, position: Int) :
        PlayerState(track, false, millisToMMSS(position))

    class Stopped(track: Track?) :
        PlayerState(track, false, millisToMMSS(0))
}