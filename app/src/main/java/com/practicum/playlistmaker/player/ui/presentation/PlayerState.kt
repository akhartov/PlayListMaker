package com.practicum.playlistmaker.player.ui.presentation

sealed class PlayerState(
    val isEnabled: Boolean,
    val isPlaying: Boolean,
    val trackTimePosition: String
) {
    class Default() :
        PlayerState(false,false, "00:00")
    class Loaded() :
        PlayerState(true,false, "00:00")

    class Playing(position: String) :
        PlayerState(true, true, position)

    class Paused(position: String) :
        PlayerState(true, false, position)

    class Stopped() :
        PlayerState(true, false, "00:00")
}