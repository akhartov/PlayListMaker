package com.practicum.playlistmaker.player.domain.api

import androidx.lifecycle.LiveData

interface AudioPlayer {

    fun open(linkUrl: String)

    fun play()
    fun pause()
    fun stop()

    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int

    fun observeTrackPlayingState(): LiveData<TrackPlayingState>
}