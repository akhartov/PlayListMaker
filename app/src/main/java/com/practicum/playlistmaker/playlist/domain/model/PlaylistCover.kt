package com.practicum.playlistmaker.playlist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistCover(
    val id: Int,
    val title: String,
    val description: String,
    val tracksCountText: String,
    val tracksMinutesText: String,
    val imagePath: String,
) : Parcelable