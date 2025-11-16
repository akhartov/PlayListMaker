package com.practicum.playlistmaker.playlist.domain

sealed interface PlaylistsEvent {
    class NewPlaylist(val title: String): PlaylistsEvent
}