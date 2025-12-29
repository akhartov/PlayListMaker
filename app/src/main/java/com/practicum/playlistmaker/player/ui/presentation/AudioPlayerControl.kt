package com.practicum.playlistmaker.player.ui.presentation

import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()

    fun moveToForeground()
    fun moveToNormal()
}