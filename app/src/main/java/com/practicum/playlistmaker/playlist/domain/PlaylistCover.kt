package com.practicum.playlistmaker.playlist.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistCover(
    val id: Int,
    val title: String,
    val description: String,
    val tracksInfo: String,
    val imagePath: String,
) : Parcelable