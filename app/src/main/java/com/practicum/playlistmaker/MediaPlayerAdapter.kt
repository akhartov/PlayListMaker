package com.practicum.playlistmaker

import android.media.MediaPlayer

class MediaPlayerAdapter(private val listener: MediaPlayerListener) {
    private var mediaPlayer = MediaPlayer()

    fun open(linkUrl: String) {
        mediaPlayer.setDataSource(linkUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            listener.onReadyToPlay()
        }
        mediaPlayer.setOnCompletionListener {
            listener.onStop()
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun play() {
        mediaPlayer.start()
        listener.onPlay()
    }

    fun pause() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
        listener.onPause()
    }

    fun stop() {
        mediaPlayer.stop()
        listener.onStop()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.getCurrentPosition()
    }
}