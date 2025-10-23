package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.search.domain.model.TrackMapper

sealed class PlayerState(val isPlaying: Boolean, val trackTimePosition: String) {
    class Playing(position: Int) : PlayerState(true, TrackMapper.millisToMMSS(position))
    class Paused(position: Int) : PlayerState(false, TrackMapper.millisToMMSS(position))
    class Stopped : PlayerState(false, TrackMapper.millisToMMSS(0))
}