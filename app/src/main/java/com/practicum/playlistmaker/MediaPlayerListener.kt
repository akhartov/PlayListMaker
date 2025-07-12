package com.practicum.playlistmaker

interface MediaPlayerListener {
    fun onReadyToPlay()
    fun onPlay()
    fun onPause()
    fun onStop()
}