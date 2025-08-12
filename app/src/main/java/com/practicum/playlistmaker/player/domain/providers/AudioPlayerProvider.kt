package com.practicum.playlistmaker.player.domain.providers

import com.practicum.playlistmaker.player.domain.api.AudioPlayer

interface AudioPlayerProvider {
    fun provideAudioPlayer(listener: AudioPlayer.Listener) : AudioPlayer
}