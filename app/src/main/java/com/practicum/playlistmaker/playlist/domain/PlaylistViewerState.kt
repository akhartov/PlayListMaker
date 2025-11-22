package com.practicum.playlistmaker.playlist.domain

data class PlaylistViewerState(
    val title: String,
    val description: String,
    val tracksLength: String,
    val tracksCount: String,
    val imagePath: String?,
)
