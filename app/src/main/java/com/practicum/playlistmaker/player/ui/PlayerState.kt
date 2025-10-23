package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.model.TrackMapper

sealed class PlayerState(val track: Track?, val isPlaying: Boolean, val trackTimePosition: String) {
    class Loaded(track: Track?) :
        PlayerState(track, false, TrackMapper.millisToMMSS(0))

    class Playing(track: Track?, position: Int) :
        PlayerState(track, true, TrackMapper.millisToMMSS(position))

    class Paused(track: Track?, position: Int) :
        PlayerState(track, false, TrackMapper.millisToMMSS(position))

    class Stopped(track: Track?) :
        PlayerState(track, false, TrackMapper.millisToMMSS(0))
}