package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.network.MediaPlayerService
import com.practicum.playlistmaker.domain.api.AudioPlayer
import com.practicum.playlistmaker.domain.providers.AudioPlayerProvider

class MediaPlayerProvider: AudioPlayerProvider {
    override fun provideAudioPlayer(listener: AudioPlayer.Listener): AudioPlayer {
        return MediaPlayerService(listener)
    }
}