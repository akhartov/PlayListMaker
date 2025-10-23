package com.practicum.playlistmaker.player.data.network

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.player.domain.api.AudioPlayer
import com.practicum.playlistmaker.player.domain.api.TrackPlayingState

class AudioPlayerImpl(private var mediaPlayer: MediaPlayer) : AudioPlayer {
    private val trackPlayingState = MutableLiveData<TrackPlayingState>(TrackPlayingState.NoTrack)
    override fun observeTrackPlayingState(): LiveData<TrackPlayingState> = trackPlayingState

    override fun open(linkUrl: String) {
        completePreviousSession()
        mediaPlayer.setDataSource(linkUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            trackPlayingState.postValue(TrackPlayingState.ReadyToPlay)
        }
        mediaPlayer.setOnCompletionListener {
            trackPlayingState.postValue(TrackPlayingState.Completed)
        }
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
    }

    override fun stop() {
        mediaPlayer.stop()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.getCurrentPosition()
    }

    private fun completePreviousSession() {
        mediaPlayer.stop()
        mediaPlayer.reset()
    }
}