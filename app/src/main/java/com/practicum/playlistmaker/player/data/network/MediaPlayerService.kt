package com.practicum.playlistmaker.player.data.network

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.AudioPlayer
import com.practicum.playlistmaker.player.domain.api.AudioPlayer.Listener

class MediaPlayerService: AudioPlayer {
    private var mediaPlayer = MediaPlayer()
    private var listener: Listener? = null

    override fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun open(linkUrl: String) {
        mediaPlayer.setDataSource(linkUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            listener?.onReadyToPlay()
        }
        mediaPlayer.setOnCompletionListener {
            listener?.onStop()
        }
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun play() {
        mediaPlayer.start()
        listener?.onPlay()
    }

    override fun pause() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
        listener?.onPause()
    }

    override fun stop() {
        mediaPlayer.stop()
        listener?.onStop()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.getCurrentPosition()
    }
}