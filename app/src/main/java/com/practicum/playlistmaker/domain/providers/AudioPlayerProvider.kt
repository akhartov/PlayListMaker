package com.practicum.playlistmaker.domain.providers

import com.practicum.playlistmaker.domain.api.AudioPlayer

interface AudioPlayerProvider {
    fun provideAudioPlayer(listener: AudioPlayer.Listener) : AudioPlayer
}