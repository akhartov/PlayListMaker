package com.practicum.playlistmaker.player.data.providers

import com.practicum.playlistmaker.player.data.network.MediaPlayerService
import com.practicum.playlistmaker.player.domain.api.AudioPlayer
import com.practicum.playlistmaker.player.domain.providers.AudioPlayerProvider

class MediaPlayerProvider: AudioPlayerProvider {
    override fun provideAudioPlayer(): AudioPlayer {
        return MediaPlayerService()
    }
}