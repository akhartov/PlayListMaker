package com.practicum.playlistmaker.player.ui

sealed interface PlayerState {
    object ReadyToPlay: PlayerState
    object Play: PlayerState
    object Pause: PlayerState
    object Stop: PlayerState
}