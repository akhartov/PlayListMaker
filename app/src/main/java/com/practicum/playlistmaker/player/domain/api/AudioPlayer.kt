package com.practicum.playlistmaker.player.domain.api

interface AudioPlayer {

    fun open(linkUrl: String)

    fun play()
    fun pause()
    fun stop()

    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int

    fun setListener(listener: Listener)

    interface Listener {
        fun onReadyToPlay()
        fun onPlay()
        fun onPause()
        fun onStop()
    }
}