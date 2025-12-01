package com.practicum.playlistmaker.playlist.data.dto

data class CoverStatisticsDto(
    val id: Int,
    val insertTime: Long,
    val title: String,
    val description: String,
    val coverFilename: String,
    val tracksCount: Int,
    val tracksDurationMilliseconds: Long
)
