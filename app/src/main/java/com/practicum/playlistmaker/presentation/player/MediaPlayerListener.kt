package com.practicum.playlistmaker.presentation.player

interface MediaPlayerListener {
    fun onReadyToPlay()
    fun onPlay()
    fun onPause()
    fun onStop()
}