package com.practicum.playlistmaker.search.data.dto

class ItunesResponse (
    val resultCount: Int,
    val results: ArrayList<TrackDto>
): Response()