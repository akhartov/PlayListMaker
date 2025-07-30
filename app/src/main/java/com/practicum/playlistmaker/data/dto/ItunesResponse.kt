package com.practicum.playlistmaker.data.dto

class ItunesResponse (
    val resultCount: Int,
    val results: ArrayList<TrackDto>
): Response()